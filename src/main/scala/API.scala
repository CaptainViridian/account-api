import config.Config
import controllers.RootController

object API extends App {
  Config.setupConfig()
  RootController.setup()

  println("Account API listening at Port 5000!")
}
