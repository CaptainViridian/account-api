package service

import dao.AccountDao
import model.Account

case class AccountService() {
  private val dao = AccountDao()

  def withdraw(accountId: String, amount: Int): Either[String, Account] = dao.getById(accountId) match {
    case Right(Account(id, balance)) =>
      val updated = dao.createOrUpdate(Account(accountId, balance - amount))
      Right(updated)
    case Left(message) => Left(message)
  }

  def deposit(accountId: String, amount: Int): Either[Nothing, Account] = dao.getById(accountId) match {
    case Right(Account(id, balance)) =>
      val updated = dao.createOrUpdate(Account(accountId, balance + amount))
      Right(updated)
    case Left(_) =>
      val created = dao.createOrUpdate(Account(accountId, balance = amount))
      Right(created)
  }
}
