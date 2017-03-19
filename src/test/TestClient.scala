package test

import server.PrivateServer
import utils.Log._

/**
  * Created by dog on 10/17/16.
  */
object TestClient {
  def main(args: Array[String]) {

    new PrivateServer(encryptMode = "ssl").start()
    log("private Server start")
  }
}
