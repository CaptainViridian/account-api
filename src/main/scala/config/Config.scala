package config

import spark.Spark.{before, port}

object Config {
  System.err.close()

  def setupConfig(): Unit = {
    port(5000)
    before((req, res) => res.`type`("text/html"))
  }
}
