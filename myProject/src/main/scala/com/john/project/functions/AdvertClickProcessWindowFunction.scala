package com.john.project.functions

import com.john.project.bean.{AdvertClickCount, AdvertClickData}
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class AdvertClickProcessWindowFunction extends ProcessWindowFunction[AdvertClickData, (AdvertClickCount, String), String, TimeWindow]{
  override def process(key: String, context: Context, elements: Iterable[AdvertClickData], out: Collector[(AdvertClickCount, String)]): Unit = {
    val arr: Array[String] = key.split(" ")
    out.collect((AdvertClickCount(arr(0).toLong, arr(1).toLong, elements.size), context.window.getStart + "->" + context.window.getEnd))
  }
}
