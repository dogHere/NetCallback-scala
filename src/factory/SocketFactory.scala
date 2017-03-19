package factory

import java.net.Socket
import javax.net.ssl.SSLSocketFactory
import utils.Log._
/**
  * Created by dog on 10/19/16.
  */
class SocketFactory(val mode:String="ssl") {

  private def createSSLSocket(): Socket ={

    SSLSocketFactory.getDefault.createSocket()
  }

  private def createSocket():Socket={
    new Socket()
  }

  def getSocket():Socket = {
    mode match {
      case "ssl" => createSSLSocket()
      case "default" => createSocket()
      case _ => createSocket()
    }
  }
}
