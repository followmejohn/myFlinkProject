package com.john.project.service

import com.john.project.bean.LoginData
import com.john.project.dao.LoginFailDao
import com.john.project.functions.MyPatternSelectFunction
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

class LoginFailService {
  private val loginFailDao = new LoginFailDao
  def getLoginFail(num: Int, dataPath: String)={
    val sourceStream: DataStream[String] = loginFailDao.readTextFile(dataPath)
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
    pStream.select(new MyPatternSelectFunction)
  }
}
