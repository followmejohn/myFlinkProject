package com.john.project.service

import com.john.project.bean.UserBehavior
import com.john.project.dao.UniqueViewDao
import com.john.project.functions.{UniqueViewByBLFunction, UniqueViewProcessWindowFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

class UniqueViewService {
  private val uniqueViewDao = new UniqueViewDao
  def getUV(dataPath: String) ={
    val sourceStream: DataStream[String] = uniqueViewDao.readTextFile(dataPath)
    sourceStream.map{ line =>
      val arr: Array[String] = line.split(",")
      UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong * 1000)
    }.assignAscendingTimestamps(_.timestamp)
      .filter(_.behavior == "pv")
      .map(data => ("uv",data.userId))
      .keyBy(_._1)
      .timeWindow(Time.minutes(60))
      .process(new UniqueViewProcessWindowFunction)
  }
  def getUVByBL(dataPath: String) ={
    val sourceStream: DataStream[String] = uniqueViewDao.readTextFile(dataPath)
    sourceStream.map{ line =>
      val arr: Array[String] = line.split(",")
      UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong * 1000)
    }.assignAscendingTimestamps(_.timestamp)
      .filter(_.behavior == "pv")
      .map(data => ("uv",data.userId))
      .keyBy(_._1)
      .timeWindow(Time.minutes(60))
      .trigger(new Trigger[(String, Long), TimeWindow] {
        override def onElement(element: (String, Long), timestamp: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = {
          TriggerResult.FIRE_AND_PURGE
        }

        override def onProcessingTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = {
          TriggerResult.CONTINUE
        }

        override def onEventTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = {
          TriggerResult.CONTINUE
        }

        override def clear(window: TimeWindow, ctx: Trigger.TriggerContext): Unit = {

        }
      }).process(new UniqueViewByBLFunction)
  }
}
