package client

import java.net.{Socket, InetSocketAddress, ServerSocket}

import factory.{SocketFactory, ServerSocketFactory}

import socks.SocksServer
import message.Message.TRANSFORM
import utils.Forward
import utils.Log._

/**
  * Created by dog on 10/18/16.
  */
class Client(val listenAddress:String = "127.0.0.1",
             val listenPort:Int=1080,
             val serviceHost:String = "localhost",
             val servicePort:Int = 3008,
             val encryptMode:String = "ssl") extends Thread{

  //val ss = new ServerSocket()
  val ssFactory =new ServerSocketFactory(encryptMode)
  val sFactory = new SocketFactory(encryptMode)


  val ss = new ServerSocket()

  ss.bind(new InetSocketAddress(listenAddress,listenPort))
  log("client socks server bind "+listenAddress+":"+listenPort)
  override def run(): Unit ={
    while (true) {
      val accepted = ss.accept()
      log("client accepted a socket from " +
        accepted.getInetAddress.getHostName+":"+accepted.getPort)
      new Thread() {
        override def run() {
          //val connection = new Socket();
          val connection = sFactory.getSocket()

          connection.connect(new InetSocketAddress(serviceHost, servicePort))
          log("client create a new socket connect to service "+serviceHost+":"+servicePort)
          val (in, out) = (connection.getInputStream, connection.getOutputStream)
          out.write(TRANSFORM)
          log("client send transform msg ")
          new Forward(accepted, connection).begin
          log("client new forward begin")
        }
      }.start()
    }
  }

  def begin(): Unit ={
    this.start()
  }
}
