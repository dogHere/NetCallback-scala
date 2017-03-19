package server

import java.net.{Socket, InetSocketAddress, ServerSocket}
import java.util.concurrent.ArrayBlockingQueue

import factory.{SocketFactory, ServerSocketFactory}
import utils.Close._
import message.Message._
import utils.{Ping, Forward}
import utils.Inet._
import utils.Log.log


/**
  * Created by dog on 10/17/16.
  */
class PublicServer(val serviceHost:String = "localhost",
                   val servicePort:Int    = 3008       ,
                   val encryptMode:String="ssl" ) extends Thread{
  val ssFactory =new ServerSocketFactory(encryptMode)
  val sFactory = new SocketFactory(encryptMode)



  val serviceSocket = ssFactory.getServerSocket()
  serviceSocket.setReuseAddress(true)
  serviceSocket.bind(new InetSocketAddress(serviceHost,servicePort))

  val queue = new ArrayBlockingQueue[Array[Byte]](500)






  override def run(): Unit ={


    while (true) {
      val rsvSocket = serviceSocket.accept()

      new Thread() {
        override def run() {
          val header = new Array[Byte](1)
          rsvSocket.getInputStream.read(header)
          log("public server accept a socket .it's header is " + header.toList)
          header(0) match {
            case CONNECT => dealConnect(rsvSocket)
            case TRANSFORM => dealTransform(rsvSocket)
            case PING => log("public server rsv ping ")
            case _ => close(rsvSocket)
          }
        }
      }.start()
    }
  }


  def begin(): Unit ={
    this.start()
  }

  def dealTransform(clientSocket: Socket): Unit = {
    log("public server accepted a socket from client then ready to transform")
    val (in, out) = (clientSocket.getInputStream, clientSocket.getOutputStream)


    var port = 10000
    var ss: ServerSocket = null;
    while (ss == null) {
      try {
        //ss = new ServerSocket(port)

        ss = ssFactory.getServerSocket(port)// todo
        //ss.bind(new InetSocketAddress(serviceHost,port))
        ss.setReuseAddress(true)

      } catch {
        case e:Exception => {
          port += 1
          //e.printStackTrace()
        }
      }
    }
    log("public server chose a port and put queue to tell private server to connect him for client transform,port is " + port)
    queue.put(getArray(port))
    val callbackSocket:Socket = null
    try {
      val callbackSocket = ss.accept()
      val (callbackIn, callbackOut) = (callbackSocket.getInputStream, callbackSocket.getOutputStream)
      val callbackArr = new Array[Byte](1)
      callbackIn.read(callbackArr)

      ss.close()

      callbackArr(0) match {
        case CALLBACK => {
          log("public server rsv a socket from private server to him for client transform")
          new Forward(clientSocket, callbackSocket).begin
          log("public server begin to transform")
        }
        case _ => log("public server callback array match missed :" + callbackArr.toList)
      }

    }catch {
      case e=>{
        close(clientSocket,callbackSocket)
      }
    }




  }

  def dealConnect(rsvSocket: Socket): Unit ={
    log("public server  dealConnect ...")
    log("public server accepted a socket connected from "+rsvSocket.getInetAddress.getHostName + ":"+rsvSocket.getPort)
    val (in,out) = (rsvSocket.getInputStream,rsvSocket.getOutputStream)
    //new Ping(out).begin()
    log("public server Ping thread started.")
    new Thread{
      override def run(): Unit ={
        while (true) {
          log("public server is waiting for client's connection ")
          out.write( Array[Byte](TRANSFORM)++ queue.take())
          log("public server toked a client " +
            "connection from queue and wrote this msg to private server")
          log("public server queue size is "+queue.size())

        }
      }
    }.start()

  }


}


