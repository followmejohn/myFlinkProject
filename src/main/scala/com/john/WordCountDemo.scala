package com.john

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object WordCountDemo {
  case class Words(word: String, count: Int){

  }
  def main(args: Array[String]): Unit = {
    val evn: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    evn.setParallelism(1)
    val sourceStream: DataStream[String] = evn.socketTextStream("hadoop203",9998,'\n')
    val wordCount: DataStream[Words] = sourceStream.flatMap(s => s.split("\\s"))
      .map(w => Words(w, 1)).keyBy("word")
      .timeWindow(Time.seconds(5))
      .sum("count")
    wordCount.print
    evn.execute()
  }
}
