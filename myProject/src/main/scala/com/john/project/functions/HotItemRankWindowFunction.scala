package com.john.project.functions

import com.john.project.bean.ItemViewCount
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class HotItemRankWindowFunction extends WindowFunction[Long, ItemViewCount, Long, TimeWindow]{
  override def apply(key: Long, window: TimeWindow, input: Iterable[Long], out: Collector[ItemViewCount]): Unit = {
    // 聚合后的数据缺失窗口信息，那么无法进行窗口内排序操作，所以，转换结构时，需要增加窗口信息，用于排序操作
    out.collect(ItemViewCount(key, input.iterator.next(), window.getEnd ))
  }
}
