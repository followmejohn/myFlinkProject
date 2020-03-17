package com.john.project.service

import com.john.project.bean.UserBehavior
import com.john.project.dao.PageViewDao
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

class PageViewService {
private val pageView = new PageViewDao
  def getPV(dataPath: String)={
    val sourceStream: DataStream[String] = pageView.readTextFile(dataPath)
    sourceStream.map{ line =>
      val arr: Array[String] = line.split(",")
      UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong * 1000)
    }.assignAscendingTimestamps(_.timestamp)
      .filter(_.behavior == "pv")
      .map(data => ("pv",1))
      .keyBy(_._1)
      .timeWindow(Time.minutes(60))
      .sum(1)
  }
}
