package com.john.project.functions

import java.sql.Timestamp

import com.john.project.bean.ItemViewCount
import org.apache.flink.api.common.state.ListStateDescriptor
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

class HotItemRankProcessFunction(num: Int) extends KeyedProcessFunction[Long, ItemViewCount, String]{
  lazy val itemStates = getRuntimeContext.getListState(
    new ListStateDescriptor[ItemViewCount]("item",classOf[ItemViewCount])
  )
  override def processElement(value: ItemViewCount,
                              ctx: KeyedProcessFunction[Long, ItemViewCount, String]#Context,
                              out: Collector[String]): Unit = {
    itemStates.add(value)
    ctx.timerService().registerEventTimeTimer(value.windowEnd)
  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Long, ItemViewCount, String]#OnTimerContext,
                       out: Collector[String]): Unit = {
    val list: ListBuffer[ItemViewCount] = new ListBuffer()
    import scala.collection.JavaConversions._
    itemStates.get().iterator().foreach{ it =>
      list += it
    }
    itemStates.clear()
    val sortedItems = list.sortBy(-_.count).take(num)
    val result = new StringBuilder
    result
      .append("==================================")
      .append("时间： ")
      .append(new Timestamp(timestamp))
      .append("\n")
    for (i <- sortedItems.indices) {
      val currentItem = sortedItems(i)
      result
        .append("No")
        .append(i + 1)
        .append(": ")
        .append(" 商品ID = ")
        .append(currentItem.itemId)
        .append(" 浏览量 = ")
        .append(currentItem.count)
        .append("\n")
    }
    result
      .append("===================================")
    Thread.sleep(1000)
    out.collect(result.toString)

  }
}
