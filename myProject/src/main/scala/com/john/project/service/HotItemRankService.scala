package com.john.project.service

import com.john.project.bean.UserBehavior
import com.john.project.dao.HotItemRankDao
import com.john.project.functions.{HotItemRankAggFunction, HotItemRankProcessFunction, HotItemRankWindowFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

class HotItemRankService {
  private val hotItemRankDao = new HotItemRankDao
  def getHotItemRank(num: Int, dataPath: String) = {
    val sourceStream: DataStream[String] = hotItemRankDao.readTextFile(dataPath)
    val windowStream: WindowedStream[UserBehavior, Long, TimeWindow] = sourceStream.map { line =>
      val arr: Array[String] = line.split(",")
      UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong * 1000)
    }.assignAscendingTimestamps(_.timestamp)
      .filter(_.behavior == "pv")
      .keyBy(_.itemId)
      .timeWindow(Time.minutes(60), Time.minutes(5))
    windowStream.aggregate(new HotItemRankAggFunction, new HotItemRankWindowFunction)
      .keyBy(_.windowEnd)
      .process(new HotItemRankProcessFunction(num))

  }

}
