package com.john


import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import scala.collection.Map
object Flink03_CEPExample {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val sourceStream: DataStream[LoginEvent] = env.fromElements(
      LoginEvent("1", "192.168.0.1", "fail", "1"),
      LoginEvent("1", "192.168.0.2", "fail", "2"),
      LoginEvent("1", "192.168.0.3", "fail", "3"),
      LoginEvent("2", "192.168.0.4", "success", "4")
    )
    val kStream: KeyedStream[LoginEvent, String] = sourceStream.assignAscendingTimestamps(_.eventTime.toLong * 1000).keyBy(_.user)
    val pattern = Pattern.begin[LoginEvent]("first").where(_.eventType == "fail")
      .next("second").where(_.eventType == "fail")
      .next("third").where(_.eventType == "fail")
      .within(Time.seconds(10))
    val patternStream: PatternStream[LoginEvent] = CEP.pattern(kStream,pattern)
    patternStream.select(
      (ps: Map[String, Iterable[LoginEvent]])=> {
        val first= ps.getOrElse("first",null).iterator.next()
        val second = ps.getOrElse("second", null).iterator.next()
        val third = ps.getOrElse("third", null).iterator.next()
        "user是： " + first.user + " 的用户， 10s 之内连续登录失败了三次，" + "ip地址分别是： " + first.ip + "; " + second.ip + "; " + third.ip
      }
    ).print()
    env.execute()
  }

  case class LoginEvent(user: String, ip: String, eventType: String, eventTime: String)

}
