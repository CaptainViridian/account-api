package dao

import data.DataStorage
import model.Account

case class AccountDao() {
  private val db = DataStorage

  def getById(id: String): Either[String, Account] = db.retrieve(id) match {
    case Some(account) => Right(account)
    case None => Left("No such account")
  }

  def createOrUpdate(account: Account): Account = {
    db.put(account)
    account
  }
}
