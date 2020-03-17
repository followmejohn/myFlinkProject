package com.john.project.service

import com.john.project.bean.{OrderEvent, TxMatchData}
import com.john.project.dao.TxMatchDao
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

class TxMatchService {
  private val txMatchDao = new TxMatchDao
  def getTx(dataPath1: String, dataPath2: String)={
    val orderStream: DataStream[String] = txMatchDao.readTextFile(dataPath1)
    val txStream: DataStream[String] = txMatchDao.readTextFile(dataPath2)
    val payStream: KeyedStream[OrderEvent, String] = orderStream.map { line =>
      val arr: Array[String] = line.split(",")
      OrderEvent(arr(0), arr(1), arr(2), arr(3).toLong * 1000)
    }.filter(_.txId != "")
      .assignAscendingTimestamps(_.timestamp)
      .keyBy(_.txId)
    val tx: KeyedStream[TxMatchData, String] = txStream.map { line =>
      val arr: Array[String] = line.split(",")
      TxMatchData(arr(0), arr(1), arr(2).toLong * 1000)
    }.assignAscendingTimestamps(_.timestamp)
      .keyBy(_.txId)
    payStream.intervalJoin(tx)
      .between(Time.minutes(-5), Time.minutes(5))
      .process(new ProcessJoinFunction[OrderEvent, TxMatchData,(OrderEvent, TxMatchData)] {
        override def processElement(left: OrderEvent, right: TxMatchData,
                                    ctx: ProcessJoinFunction[OrderEvent, TxMatchData, (OrderEvent, TxMatchData)]#Context,
                                    out: Collector[(OrderEvent, TxMatchData)]): Unit = {
          out.collect(left, right)
        }
      })
  }
}
