package com.john.project.functions

import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable

class UniqueViewProcessWindowFunction extends ProcessWindowFunction[(String, Long), (String, Long), String, TimeWindow]{
  override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[(String, Long)]): Unit = {
    val set = mutable.Set[Long]()
//    for(i <- elements){
//      set.add(i._2)
//    }
    elements.iterator.foreach(it =>
      set.add(it._2)
    )
    out.collect(context.window.getEnd + "", set.size)
  }
}
