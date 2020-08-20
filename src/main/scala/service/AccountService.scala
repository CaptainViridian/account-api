package service

import dao.AccountDao
import model.Account

case class AccountService() {
  private val dao = AccountDao()

  def checkBalance(accountId: String): Either[Unit, Int] = dao.getById(accountId) match {
    case Right(Account(_, balance)) => Right(balance)
    case Left(_) => Left(())
  }

  def withdraw(accountId: String, amount: Int): Either[Unit, Account] = dao.getById(accountId) match {
    case Right(Account(id, balance)) =>
      val updated = dao.createOrUpdate(Account(accountId, balance - amount))
      Right(updated)
    case Left(_) => Left(())
  }

  def deposit(accountId: String, amount: Int): Either[Nothing, Account] = dao.getById(accountId) match {
    case Right(Account(id, balance)) =>
      val updated = dao.createOrUpdate(Account(accountId, balance + amount))
      Right(updated)
    case Left(_) =>
      val created = dao.createOrUpdate(Account(accountId, balance = amount))
      Right(created)
  }

  def transfer(originId: String, destinationId: String, amount: Int) = this.withdraw(originId, amount) match {
    case Right(originAccount) => deposit(destinationId, amount) match {
      case Right(destinationAccount) => Right((originAccount, destinationAccount))
    }
    case Left(_) => Left(())
  }
}
