package com.john.project.service

import com.john.project.bean.AdvertClickData
import com.john.project.dao.AdvertClickAnalysisDao
import com.john.project.functions.{AdvertClickKeyedProcessFunction, AdvertClickProcessWindowFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

class AdvertClickAnalysisService {
  private val advertClickAnalysisDao = new AdvertClickAnalysisDao
  def getAdvertClick(dataPath: String)={
    val sourceStream: DataStream[String] = advertClickAnalysisDao.readTextFile(dataPath)
    val eltStream: DataStream[AdvertClickData] = sourceStream.map { line =>
      val arr: Array[String] = line.split(",")
      AdvertClickData(arr(0).toLong, arr(1).toLong, arr(2), arr(3), arr(4).toLong * 1000 )
    }.assignAscendingTimestamps(_.timestamp)
      .keyBy(date => date.userId + " " + date.advId)
      .process(new AdvertClickKeyedProcessFunction)
    eltStream.getSideOutput(new OutputTag[String]("backList")).print("backList>>>")
    eltStream.keyBy(date => date.userId + " " + date.advId)
      .timeWindow(Time.hours(1), Time.seconds(5))
      .process(new AdvertClickProcessWindowFunction)
  }
}
