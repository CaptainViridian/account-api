package data

import model.Account

case class DataStorage() {
  val storage = scala.collection.mutable.Map[Int, Account]()

  def put(account: Account): Option[Account] = account match {
    case Account(id, _) => storage.put(id, account)
  }

  def retrieve(id: Int): Option[Account] = storage.get(id)
}
