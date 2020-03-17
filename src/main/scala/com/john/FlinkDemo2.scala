package com.john

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala._

object FlinkDemo2 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
//    val sourceStream: DataStream[(String, List[String])] = env.fromElements(("aa",List("A")),("bb",List("B")),("bb",List("C")),("cc",List("D")),("aa",List("E")),("cc",List("F")))
//    sourceStream.keyBy(_._1)
//      .reduce((x,y) => (x._1,x._2 ::: y._2))
//      .print()
    val sourceDate: DataStream[Int] = env.fromCollection(List(1,2,3,4,5,6,7,8))
    sourceDate.map((_,1)).keyBy(_._1).map((_,"a")).print()
    env.execute()

  }
}
