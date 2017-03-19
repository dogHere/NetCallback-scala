package utils

import java.io.{OutputStream, InputStream}

/**
  * Created by dog on 10/17/16.
  */
class Transform(val in:InputStream,val out:OutputStream) extends Thread{

  override def run(): Unit ={
    val buffer = new Array[Byte](1024)
    var len = 0
    try {
      while ( {
        len = in.read(buffer)
        len != -1
      }) {
        out.write(buffer, 0, len)
        out.flush()
        //println(buffer.toList)
        //print(new String(buffer,0,len))

      }
      out.close()
      in.close()
    }catch {
      case _=>
    }
  }

  def begin: Unit ={
    this.start()
  }
}
