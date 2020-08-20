package http

import io.circe.Json
import io.circe.syntax._
import spark.Response

object HttpResponse {
  def ok(jsonBody: Json)(implicit res: Response): Json = {
    res.status(200)
    jsonBody
  }

  def created(jsonBody: Json)(implicit res: Response): Json = {
    res.status(201)
    jsonBody
  }

  def notFound()(implicit res: Response): Json = {
    res.status(404)
    0.asJson
  }

  def badRequest()(implicit res: Response): Json = {
    res.status(400)
    ().asJson
  }
}
