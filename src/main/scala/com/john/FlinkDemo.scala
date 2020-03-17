package com.john
import org.apache.flink.streaming.api.scala._
object FlinkDemo {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    // 滚动聚合的例子
//    val sourceStream: DataStream[(Int, Int, Int)] = env.fromElements((1,2,3),(2,4,2),(2,9,8),(1,6,9))
//      sourceStream
//      .keyBy(0)
//      .sum(1)
//      .print()
    val sourceDate: DataStream[MyAnimalAge] = env.addSource(new MySource)
    sourceDate.print()
    env.execute()
  }
}
