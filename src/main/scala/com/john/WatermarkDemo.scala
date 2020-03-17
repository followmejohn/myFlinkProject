package com.john
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
object WatermarkDemo {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    //设置为事件时间
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val sourceStream: DataStream[String] = env.readTextFile("./input/source.txt")
    sourceStream.map(_.split(","))
      .map(a =>(a(0),a(1).toLong, a(2)))
      .assignTimestampsAndWatermarks(
        new BoundedOutOfOrdernessTimestampExtractor[(String, Long, String)](Time.seconds(1)) {//设置最大延时为1s
        override def extractTimestamp(element: (String, Long, String)): Long = element._2 * 1000 //读取数据为毫秒
      })
      .keyBy(_._1)
      .timeWindow(Time.seconds(5))
//      .reduce((x, y) => (x._1,x._2,x._3 + " " + y._3))
      .minBy(2)
      .print()
    env.execute()
  }
}
