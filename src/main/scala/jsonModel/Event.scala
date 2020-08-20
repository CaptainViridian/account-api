package jsonModel

import io.circe.{Decoder, HCursor}

case class Event(action: String, amount: Int, origin: Option[String], destination: Option[String])

object Event {
  val decoder: Decoder[Event] = (hCursor: HCursor) => for {
    action <- hCursor.get[String]("type")
    amount <- hCursor.get[Int]("amount")
    origin <- hCursor.get[Option[String]]("origin")
    destination <- hCursor.get[Option[String]]("destination")
  } yield Event(action, amount, origin, destination)
}
