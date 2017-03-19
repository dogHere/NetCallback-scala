package utils

/**
  * Created by dog on 10/19/16.
  */
object Log {
  def log(msg:String): Unit ={
    println(System.currentTimeMillis()+": "+msg)
  }
}
