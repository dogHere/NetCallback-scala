package command

import java.util.Properties

import client.Client
import server.{PrivateServer, PublicServer}
import utils.Log.log
/**
  * Created by dog on 10/19/16.
  */
object Command {

  val help =  """
          |Usage: scala throughFirewall.jar [OPTION]... [FILE]
          |Build a tunnel to connect to firewall behind .
          |
          |Options:
          | -h, --help show this help msg and exit.
          | -public    use public server .
          | -private   use private server .
          | -client    use client socks server default.
          |
          |Examples:
          | scala throughFirewall.jar -public  -service 192.168.0.100:3008
          | scala throughFirewall.jar -private -service 192.168.0.100:3008
          | scala throughFirewall.jar -client  -listen  127.0.0.1:1080 -service 192.168.0.100:3008
          |
          |Please put the ssl keystore under the program dir .
          |
        """.stripMargin

    val helpPublic =
      """
        |Usage: scala throughFirewall.jar -public [OPTION]... [FILE]
        |Start a public server to forward streams to firewall behind .
        |
        |Options:
        | -h, --help            show this help msg and exit .
        | -service [host:port]  public server listen host and port, default is localhost:3008 .
        | -ssl                  encrypt with ssl, default is ssl,param default without encrypt .
        | -key FILE             keystore ,default is black.
        |
        | Examples:
        |  scala throughFirewall.jar -public  -service 192.168.0.100:3008
        |
      """.stripMargin

    val helpPrivate =
      """
        |Usage:scala throughFirewall.jar -private [OPTION]... [FILE]
        |Start a private server to forward streams from remote public server .
        |
        | -h, --help            show this help msg and exit .
        | -service [host:port]  public server listen host and port, default is localhost:3008 .
        | -ssl                  encrypt with ssl, default is ssl,param default without encrypt .
        | -key FILE             keystore ,default is black.
        |
        | Examples:
        |  scala throughFirewall.jar -private -service 192.168.0.100:3008
        |
      """.stripMargin

    val helpClient =
      """
        |Usage:scala throughFirewall.jar -private [OPTION]... [FILE]
        |Start a client to forward local streams to remote public server .
        |
        |Options:
        | -h,--help              show this help msg and exit .
        | -listen [host:port]    local socks server listen host and port,default is localhost:1080 .
        | -service [host:port]   public server listen host and port, default is localhost:3008 .
        | -ssl                   encrypt with ssl, default is ssl,param default without encrypt .
        | -key FILE              keystore ,default is black.
        |
        | Examples:
        |  scala throughFirewall.jar -client  -listen  127.0.0.1:1080 -service 192.168.0.100:3008
        |
      """.stripMargin


  def main(args: Array[String]) {


      System.setProperty("javax.net.ssl.keyStore","keystore")
      System.setProperty("javax.net.ssl.trustStore","keystore")
      System.setProperty("javax.net.ssl.keyStorePassword","[55tt520][55tt520]")
      try {
        if (args.length == 0) {
          println(help)
        } else if (args.length == 1) {
          matchArgs0(args)
        } else {
          var mode = ""
          var listenHost = ""
          var listenPort = -1
          var serviceHost = ""
          var servicePort = -1

          for (i <- 0 to args.length - 1) {
            if (args(i).equals("-client") || args(i).equals("-private") || args(i).equals("-public")) {
              mode = args(i)
            } else if (args(i).equals("-listen")) {
              val parse = args(i + 1).split(":")
              listenHost = parse(0)
              listenPort = Integer.parseInt(parse(1))
            } else if (args(i).equals("-service")) {
              val parse = args(i + 1).split(":")
              serviceHost = parse(0)
              servicePort = Integer.parseInt(parse(1))
            }
          }

          mode match {
            case "-client" => {
              log("start client socks proxy server listen " + listenHost + ":" + listenPort + " connect to " + serviceHost + ":" + servicePort)
              new Client(listenHost, listenPort, serviceHost, servicePort).begin()
            }
            case "-private" => {
              log("start private server connect to  " + serviceHost + ":" + servicePort)
              new PrivateServer(serviceHost, servicePort).begin()
            }
            case "-public" => {
              log("start public server listen " + serviceHost + ":" + servicePort)
              new PublicServer(serviceHost, servicePort).begin()
            }
            case _ => println(help)
          }
        }
      }catch {
        case e:Exception =>{
          e.printStackTrace()

          println(help)
        }

      }



  }

  private def matchArgs0(string:Array[String]):Boolean={
      string(0) match {
        case "-public" => {
          println(helpPublic)
          true
        }
        case "-private"=> {
          println(helpPrivate)
          true
        }
        case "-client" => {
          println(helpClient)
          true
        }
        case _ => {
          println(help)
          false
        }
      }
    }
}
