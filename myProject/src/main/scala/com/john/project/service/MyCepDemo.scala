package com.john.project.service

import java.util

import com.john.project.bean.LoginData
import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object MyCepDemo {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val sourceStream: DataStream[String] = env.readTextFile("F:\\myFlink0830Tutorial\\input\\LoginLog.csv")
    val keyedStream: KeyedStream[LoginData, String] = sourceStream.map { line =>
      val arr: Array[String] = line.split(",")
      LoginData(arr(0), arr(1), arr(2), arr(3).toLong * 1000)
    }.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[LoginData](Time.seconds(10)) {
      override def extractTimestamp(element: LoginData): Long = {
        element.timestamp
      }
    }).keyBy(_.userId)
    val pattern: Pattern[LoginData, LoginData] = Pattern.begin[LoginData]("first").where(_.status == "fail")
      .next("two").where(_.status == "fail")
      .within(Time.seconds(2))
    val pStream: PatternStream[LoginData] = CEP.pattern(keyedStream, pattern)
    pStream.select(new PatternSelectFunction[LoginData, String]{
      override def select(map: util.Map[String, util.List[LoginData]]): String = {
        map.toString
      }
    }).print()
    env.execute()
  }
}
