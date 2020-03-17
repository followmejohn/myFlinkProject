package com.john.project.functions

import com.john.project.bean.ApacheLogEvent
import org.apache.flink.api.common.functions.AggregateFunction

class HotPageRankAggFunction extends AggregateFunction[ApacheLogEvent, Long, Long]{
  override def createAccumulator(): Long = 0L

  override def add(value: ApacheLogEvent, accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}
