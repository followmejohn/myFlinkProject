package com.john

import org.apache.flink.streaming.api.scala._
//前后两个温度差超过1.7就报警
object Flink02_KeyedStateExample2 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val sourceStream: DataStream[SensorReading] = env.addSource(new SensorSource)
    sourceStream.keyBy(_.id)
      .flatMapWithState[(String, Double, Double), Double]{
      case (in: SensorReading, None) =>
        (List.empty,Some(in.temperature)) // Some操作将温度保存到了状态变量中
      case (in:SensorReading, Some(lastTp)) =>
        val diff: Double = (lastTp - in.temperature).abs
        if(diff > 1.7) {
          (List((in.id, lastTp, in.temperature)),Some(in.temperature))
        }else{
          (List.empty,Some(in.temperature))
        }
      }
      .print()
    env.execute()
  }
}
