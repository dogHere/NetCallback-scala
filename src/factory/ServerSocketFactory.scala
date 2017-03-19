package factory

import java.net.ServerSocket
import javax.net.ssl.SSLServerSocketFactory

/**
  * Created by dog on 10/19/16.
  */
class ServerSocketFactory(val mode:String="ssl" ) {


  def getServerSocket(): ServerSocket = {

    mode match {
      case "ssl" => createSSLServerSocket()
      case "default" => createServerSocket()
      case _ => createServerSocket()
    }
  }

  def getServerSocket(port: Int): ServerSocket = {
    mode match {
      case "ssl" => createSSLServerSocket(port)
      case "default" => createServerSocket(port)
      case _ => createServerSocket(port)
    }
  }

  private def createServerSocket(port: Int): ServerSocket = {
    val ss = new ServerSocket(port)
    return ss
  }

  private def createSSLServerSocket(port: Int) : ServerSocket = {

    val ss = SSLServerSocketFactory.getDefault.createServerSocket(port)
    return ss
  }

    private def createServerSocket(): ServerSocket = {
    val ss = new ServerSocket()
    return ss
  }

  private def createSSLServerSocket(): ServerSocket = {
    val ss = SSLServerSocketFactory.getDefault.createServerSocket()
    return ss
  }
}