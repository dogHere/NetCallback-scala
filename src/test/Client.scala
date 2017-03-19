package test

import client.Client

/**
  * Created by dog on 10/18/16.
  */
object Client {
  def main(args: Array[String]) {
    new Client(encryptMode = "ssl").begin()
  }
}
