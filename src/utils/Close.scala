package utils

import java.io.Closeable

/**
  * Created by dog on 10/18/16.
  */
object Close {
  def close(close: Closeable*): Unit ={
    close.foreach(
      c => c.close()
    )
  }
}
