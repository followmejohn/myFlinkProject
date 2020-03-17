package com.john.project.functions

import java.sql.Timestamp

import com.john.project.bean.URLClickCount
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

class HotPageRankProcessFunction(num: Int) extends KeyedProcessFunction[Long, URLClickCount, String]{
  private var listState : ListState[URLClickCount]= _
  private var valueZero : ValueState[Long] = _
  override def open(parameters: Configuration): Unit = {
    listState = getRuntimeContext.getListState(new ListStateDescriptor[URLClickCount]("list", classOf[URLClickCount]))
    valueZero = getRuntimeContext.getState(new ValueStateDescriptor[Long]("valueZero", classOf[Long]))
  }
  override def processElement(value: URLClickCount,
                              ctx: KeyedProcessFunction[Long, URLClickCount, String]#Context,
                              out: Collector[String]): Unit = {
    listState.add(value)
    if(valueZero.value() == 0){
      ctx.timerService().registerEventTimeTimer(value.windowEndTime)
      valueZero.update(value.windowEndTime)
    }

  }
  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Long, URLClickCount, String]#OnTimerContext,
                       out: Collector[String]): Unit = {
    val listBuffer = new ListBuffer[URLClickCount]
    import scala.collection.JavaConversions._
    listState.get().iterator().foreach{ it =>
      listBuffer += it
    }
    val hotUrl: ListBuffer[URLClickCount] = listBuffer.sortBy(_.count)(Ordering.Long.reverse).take(num)
    val stringBuffer = new StringBuffer()
    stringBuffer.append("===============")
      .append("时间: ")
      .append(new Timestamp(timestamp))
      .append("\n")
    hotUrl.iterator.foreach(it => {
      stringBuffer.append("url:")
        .append(it.url)
        .append("  点击量: ")
        .append(it.count)
        .append("\n")
    })
    Thread.sleep(1000)
    out.collect(stringBuffer.toString)
  }
}
