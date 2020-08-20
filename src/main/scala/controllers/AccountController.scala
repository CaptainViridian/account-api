package controllers

import http.HttpResponse

import model.{Account, Event}

import service.AccountService

import spark.{Response, Route}
import spark.Spark.{get, post}

import io.circe.{Decoder, Encoder}
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.semiauto._

case class AccountController(service: AccountService) {
  private implicit val decoder: Decoder[Event] = Event.decoder
  private implicit val encoder: Encoder[Account] = deriveEncoder

  def setup(): Unit = {
    get("/balance", handleGetBalance)
    post("/event", handlePostEvent)
  }

  val handleGetBalance: Route = (request, response) => try {
    implicit val res: Response = response

    val accountId = request.queryParams("account_id")
    service.checkBalance(accountId) match {
      case Right(balance) => HttpResponse.ok(balance.asJson)
      case Left(_) => HttpResponse.notFound()
    }
  } catch {
    case e => e.printStackTrace(System.out)
      "".asJson
  }

  val handlePostEvent: Route = (request, response) => {
    implicit val res: Response = response

    decode(request.body()) match {
      case Right(Event(action, amount, origin, destination)) => (action match {
        case "withdraw" => service.withdraw(origin.get, amount)
        case "deposit" => service.deposit(destination.get, amount)
        case "transfer" =>
          service.withdraw(origin.get, amount)
          service.deposit(destination.get, amount)
      }) match {
        case Right(responseBody) => HttpResponse.created(responseBody.asJson)
        case Left(_) => HttpResponse.notFound()
      }
      case Left(_) => HttpResponse.badRequest()
    }
  }
}
