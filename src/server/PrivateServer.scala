package server

import java.net.{ConnectException, InetSocketAddress, Socket}
import java.nio.ByteBuffer
import java.util.concurrent.ArrayBlockingQueue

import factory.{SocketFactory, ServerSocketFactory}
import utils.Close.close
import message.Message._
import utils.{Ping, Forward}
import utils.Inet._
import utils.Log._

/**
  * Created by dog on 10/17/16.
  */
class PrivateServer(val serviceHost:String="localhost",
                    val servicePort:Int = 3008,
                    val encryptMode:String="ssl") extends Thread{

  val ssFactory =new ServerSocketFactory(encryptMode)
  val sFactory = new SocketFactory(encryptMode)

  var isConnected = false

  val queue = new ArrayBlockingQueue[Array[Byte]](500)


  override def run(): Unit = {

    connect
    backgroud
    dealTransform

  }

  def backgroud(): Unit ={
    val daemon = new Thread(){
      override def run(): Unit ={
        while (true){
          Thread.sleep(1000*5)
          if(getStatus==false){
            log("private server status is not running after 5 s  try to restart itself ")
            connect()
          }
        }
      }
    }
    daemon.setDaemon(true)
    daemon.start()
  }

  def begin (): Unit ={
    this.start
  }


  def getStatus: Boolean = {
    synchronized{
      return isConnected
    }
  }

  def setStatus(boolean: Boolean): Unit = {
    synchronized{
      isConnected = boolean
    }
  }

  def connect(): Unit = {
    new Thread() {
      override def run() {
        var connection:Socket = null;

        try {
          log("private server starting connection to public server " + serviceHost + ":" + servicePort)
          connection = sFactory.getSocket()
          connection.connect(new InetSocketAddress(serviceHost, servicePort))
          //val connection = new Socket(serviceHost, servicePort)
          setStatus(true)
          log("private server status is running ")
          val (in, out) = (connection.getInputStream, connection.getOutputStream)



          out.write(Array[Byte](CONNECT))
          log("private server write connect msg to public server " + ": " + CONNECT)

          new Ping(out).begin()
          log("private server start ping thread ")

          var len = 0;
          while (true) {
            val arr = new Array[Byte](3)
            len = in.read(arr)
            log("private server rsv msg from public server :" + arr.toList)
            arr(0) match {
              case PING => log("private server rsv public ping " + arr.slice(0, len).toList)
              case TRANSFORM => {
                log("private server put transform msg to queue")
                queue.put(arr)
              }
            }
          }
        }catch {
          case _ =>
        }finally {
          if (connection!=null) close(connection)
          setStatus(false)
        }

      }
    }.start()

  }

  def dealTransform(): Unit = {
    new Thread() {
      override def run() {
        while (true) {
          log("private server is listen to the queue ,waiting for public server callback msg ...")
          val take = queue.take()


          new Thread() {
            override def run(): Unit = {
              log("private server get port array is  " + take.slice(1, 3).toList)
              val callbackPort = getPort(take.slice(1, 3))
              log("private server take a msg from queue :" + take.toList + ",it's the callback port :" + callbackPort)

              val address = new InetSocketAddress(serviceHost, callbackPort)
              log("private server connecting to public server :" + serviceHost + ":" + callbackPort)


              val callbackSocket = sFactory.getSocket()
             // val callbackSocket = new Socket()
              callbackSocket.connect(address)
              log("private server callback socket connected to public server ")
              //处理经过服务端转发的来自client的请求
              val (in, out) = (callbackSocket.getInputStream, callbackSocket.getOutputStream)

              out.write(Array[Byte](CALLBACK))
              out.flush()

              val targetAddress = socks.SocksServer.checkSocks(callbackSocket)
              log("private server connecting " + targetAddress.getHostString + ":" + targetAddress.getPort)
              val remoteSocket =new Socket() //todo

              try {
                remoteSocket.connect(new InetSocketAddress(targetAddress.getHostString, targetAddress.getPort))
                new Forward(callbackSocket, remoteSocket).begin
                log("private server begin to new forward")
              }catch {
                case _=> {
                  close(callbackSocket,remoteSocket)
                }
              }
            }
          }.start()

        }
      }
    }.start()
  }

}

