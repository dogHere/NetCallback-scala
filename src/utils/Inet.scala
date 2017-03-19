package utils

import java.nio.ByteBuffer

/**
  * Created by dog on 10/18/16.
  */
object Inet {
  def getPort(array: Array[Byte]): Int ={
    ByteBuffer.wrap(array).asShortBuffer().get() & 0xFFFF
  }

  def getArray(port:Int):Array[Byte]={
    """
      |aa
      |
      |aaassssssssssss
      |sssssssssssssssss
      |ssssssssssssss aa
      |啊啊啊啊啊啊啊啊啊啊啊啊啊啊
      |三嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖
      |嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖嗖
      |啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊
      |学习学习学习学习学习学习学习学习学习学习学习学习下
      |
    """.stripMargin
    Array[Byte]( (port>>>8).toByte,port.toByte)
  }
}
