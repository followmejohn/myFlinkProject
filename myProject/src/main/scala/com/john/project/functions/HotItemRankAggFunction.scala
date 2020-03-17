package com.john.project.functions

import com.john.project.bean.UserBehavior
import org.apache.flink.api.common.functions.AggregateFunction

class HotItemRankAggFunction extends AggregateFunction[UserBehavior, Long, Long]{
  override def createAccumulator(): Long = 0L

  override def add(value: UserBehavior, accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}
