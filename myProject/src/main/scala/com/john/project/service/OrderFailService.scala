package com.john.project.service

import com.john.project.bean.{OrderData, OrderEvent, OrderResult}
import com.john.project.dao.OrderFailDao
import com.john.project.functions.{FailOrderFunction, OrderSelectFunction}
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

class OrderFailService {
  private val orderFailDao = new OrderFailDao
  def getOrderFail(dataPath: String) ={
    val sourceStream: DataStream[String] = orderFailDao.readTextFile(dataPath)
    val keyedStream: KeyedStream[OrderData, String] = sourceStream.map { line =>
      val arr: Array[String] = line.split(",")
      OrderData(arr(0), arr(1), arr(3).toLong * 1000)
    }.assignAscendingTimestamps(_.timestamp)
      .keyBy(_.orderId)
    val pattern: Pattern[OrderData, OrderData] = Pattern.begin[OrderData]("first").where(_.event == "create")
      .followedBy("second").where(_.event == "pay")
      .within(Time.minutes(15))
    val patternStream: PatternStream[OrderData] = CEP.pattern(keyedStream, pattern)
    patternStream.select(new OutputTag[OrderResult]("failOrder"),new FailOrderFunction,new OrderSelectFunction)
  }
}
