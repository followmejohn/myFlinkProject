package com.john.project.functions

import java.text.SimpleDateFormat

import com.john.project.bean.AdvertClickData
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

class AdvertClickKeyedProcessFunction extends KeyedProcessFunction[String, AdvertClickData, AdvertClickData]{
  private var count: ValueState[Long] = _
  private var sendErrorState: ValueState[Boolean] = _
  private var resetTime: ValueState[Long] = _
  override def open(parameters: Configuration): Unit = {
    count = getRuntimeContext.getState(new ValueStateDescriptor[Long]("count", classOf[Long]))
    sendErrorState = getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("sendErrorState", classOf[Boolean]))
    resetTime = getRuntimeContext.getState(new ValueStateDescriptor[Long]("resetTimer", classOf[Long]))
  }
  override def processElement(value: AdvertClickData,
                              ctx: KeyedProcessFunction[String, AdvertClickData, AdvertClickData]#Context,
                              out: Collector[AdvertClickData]): Unit = {
    val currentCount: Long = count.value()
    if(currentCount == 0){
      if(resetTime.value() == 0){
//        val ct: Long = ctx.timerService().currentProcessingTime()
        val ct = ctx.timestamp()
        val format= new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss")
        print(format.format(value.timestamp))
//        println(ct)
        val nextd: Long = ct/(1000 * 60 * 60 * 24) + 1
        val nextDay: Long = nextd * 1000 * 60 * 60 * 24
//        ctx.timerService().registerProcessingTimeTimer(nextDay)
        ctx.timerService().registerEventTimeTimer(nextDay)
        println("这里设置了定时事件。。。。")
        resetTime.update(nextDay)
      }
    }
    if(count.value() >= 100){
      if(!sendErrorState.value()) {
        ctx.output(new OutputTag[String]("backList"), value.userId + " 点击广告" + value.advId + "数量超过100次")
        sendErrorState.update(true)
      }
    }else{
      count.update(currentCount + 1)
      out.collect(value)
    }
  }
  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[String, AdvertClickData, AdvertClickData]#OnTimerContext,
                       out: Collector[AdvertClickData]): Unit = {
    count.clear()
    sendErrorState.clear()
    resetTime.clear()
    print("触发了事件。。")
    val format= new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss")
    println(format.format(timestamp))
  }
}




















