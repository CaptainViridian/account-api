package controllers

import http.HttpResponse
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}
import jsonModel.Event
import model.Account
import service.AccountService
import spark.Spark.{get, post}
import spark.{Response, Route}

case class AccountController(service: AccountService) {
  private implicit val eventDecoder: Decoder[Event] = Event.decoder
  private implicit val accountEncoder: Encoder[Account] = deriveEncoder

  def setup(): Unit = {
    get("/balance", handleGetBalance)
    post("/event", handlePostEvent)
  }

  private val handleGetBalance: Route = (request, response) => {
    implicit val res: Response = response
    val accountId = request.queryParams("account_id")

    service.checkBalance(accountId) match {
      case Right(balance) => HttpResponse.ok(balance.asJson)
      case Left(_) => HttpResponse.notFound()
    }
  }

  private val handlePostEvent: Route = (request, response) => {
    implicit val res: Response = response

    def withdraw(origin: String, amount: Int): Json =
      service.withdraw(origin, amount) match {
        case Right(account) => HttpResponse.created(Map("origin" -> account).asJson)
        case Left(_) => HttpResponse.notFound()
      }

    def deposit(destination: String, amount: Int): Json =
      service.deposit(destination, amount) match {
        case Right(account) => HttpResponse.created(Map("destination" -> account).asJson)
      }

    def transfer(origin: String, destination: String, amount: Int): Json =
      service.withdraw(origin, amount) match {
        case Right(originAccount) => service.deposit(destination, amount) match {
          case Right(destinationAccount) =>
            HttpResponse.created(Map("origin" -> originAccount, "destination" -> destinationAccount).asJson)
        }
        case Left(_) => HttpResponse.notFound()
      }

    decode(request.body()) match {
      case Right(Event(action, amount, Some(origin), Some(destination))) => action match {
        case "withdraw" => withdraw(origin, amount)
        case "deposit" => deposit(destination, amount)
        case "transfer" => transfer(origin, destination, amount)
      }
      case Left(_) => HttpResponse.badRequest()
    }
  }
}
