package com.john.project.service

import java.text.SimpleDateFormat

import com.john.project.bean.ApacheLogEvent
import com.john.project.dao.HotPageRankDao
import com.john.project.functions.{HotPageRankAggFunction, HotPageRankProcessFunction, HotPageRankWindowFunction}
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

class HotPageRankService {
  private val hotPageRankDao = new HotPageRankDao
  def getHotPageRank(num: Int, dataPath: String)= {
    val sourceStream: DataStream[String] = hotPageRankDao.readTextFile("F:\\myFlink0830Tutorial\\input\\apache.log")
    val format = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
    val etlStream: DataStream[ApacheLogEvent] = sourceStream.map { line =>
      val arr = line.split(" ")
      val time: Long = format.parse(arr(3)).getTime
      ApacheLogEvent(arr(0), arr(1), time, arr(5), arr(6))
    }
    etlStream.assignTimestampsAndWatermarks(
      new BoundedOutOfOrdernessTimestampExtractor[ApacheLogEvent](Time.minutes(1)) {
        override def extractTimestamp(element: ApacheLogEvent): Long = element.eventTime
      }
    ).keyBy(_.url)
      .timeWindow(Time.minutes(10), Time.seconds(5))
      .aggregate(new HotPageRankAggFunction, new HotPageRankWindowFunction)
      .keyBy(_.windowEndTime)
      .process(new HotPageRankProcessFunction(num))
  }

}
