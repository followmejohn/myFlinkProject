package com.john.project.functions

import com.john.project.bean.URLClickCount
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class HotPageRankWindowFunction extends WindowFunction[Long, URLClickCount, String, TimeWindow]{
  override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[URLClickCount]): Unit = {
    out.collect(URLClickCount(key, input.iterator.next(), window.getEnd))
  }
}
