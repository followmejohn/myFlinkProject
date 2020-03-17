package com.john

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object FlinkDemo3 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    // 滚动聚合的例子
    val sourceStream= env.addSource(new SensorSource)
      sourceStream
        .map(r => (r.id,r.temperature))
        .keyBy(_._1)
        .timeWindow(Time.seconds(5))
        .reduce((x,y) => (x._1, x._2.min(y._2)))
          .print()
    env.execute()
  }
}
