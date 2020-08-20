package controllers

import spark.Route
import spark.Spark.get

import io.circe.syntax._

import service.AccountService

object RootController {
  val accountService = AccountService()

  def setup(): Unit = {
    this.setupRoot()

    val accountController = AccountController(accountService)
    accountController.setup()
  }

  private def setupRoot(): Unit = get("/", handleRoot)

  private val handleRoot: Route = (req, res) => "Welcome to the Account API!".asJson
}
