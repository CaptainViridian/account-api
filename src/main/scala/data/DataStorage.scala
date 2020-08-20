package data

import model.Account

case object DataStorage {
  private val storage = scala.collection.mutable.Map[String, Account]()

  def put(account: Account): Option[Account] = account match {
    case Account(id, _) => storage.put(id, account)
  }

  def retrieve(id: String): Option[Account] = storage.get(id)

  def reset(): Unit = storage.clear()
}
