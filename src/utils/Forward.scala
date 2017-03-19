package utils

import java.net.Socket

/**
  * Created by dog on 10/17/16.
  */
class Forward (val s1:Socket,
               val s2:Socket) extends Thread{
  val (s1Tos2,s2Tos1) = (
    new Transform(s1.getInputStream,s2.getOutputStream),
    new Transform(s2.getInputStream,s1.getOutputStream)
  )

  override def run(): Unit ={
    s1Tos2.join()
    s2Tos1.join()
    s1Tos2.begin
    s2Tos1.begin
  }

  def begin: Unit ={
    this.start()
  }
}
