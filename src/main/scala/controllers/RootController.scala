package controllers

import spark.Route
import spark.Spark.get

import io.circe.syntax._

object RootController {
  def setup(): Unit = {
    this.setupRoot()
  }

  private def setupRoot(): Unit = get("/", handleRoot)

  private val handleRoot: Route = (req, res) => "Welcome to the Account API!".asJson
}
