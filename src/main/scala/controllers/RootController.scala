package controllers

import data.DataStorage
import http.HttpResponse
import spark.Route
import spark.Spark.{get, post}
import io.circe.syntax._
import service.AccountService

object RootController {
  val accountService = AccountService()

  def setup(): Unit = {
    this.setupRoot()
    this.setupReset()

    val accountController = AccountController(accountService)
    accountController.setup()
  }

  private def setupRoot(): Unit = get("/", handleRoot)
  private def setupReset(): Unit = post("/reset", handleReset)

  private val handleRoot: Route = (req, res) => HttpResponse.ok("Welcome to the Account API!".asJson)(res)
  private val handleReset: Route = (req, res) => {
    DataStorage.reset()
    HttpResponse.ok()(res)
  }
}
