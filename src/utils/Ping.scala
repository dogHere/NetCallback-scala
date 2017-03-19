package utils

import java.io.OutputStream

import message.Message.PING
/**
  * Created by dog on 10/18/16.
  */
class Ping(out:OutputStream) extends Thread{
  override def run(): Unit ={
    try {
      while (true) {
        Thread.sleep(1000 * 3)
        out.write(PING)
        out.flush()
      }
    }catch {
      case e=>
    }
  }

  def begin(): Unit ={
    this.start()
  }
}
