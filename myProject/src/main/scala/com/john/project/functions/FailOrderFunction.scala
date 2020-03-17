package com.john.project.functions

import java.util

import com.john.project.bean.{OrderData, OrderEvent, OrderResult}
import org.apache.flink.cep.PatternTimeoutFunction

class FailOrderFunction extends PatternTimeoutFunction[OrderData, OrderResult]{
  override def timeout(map: util.Map[String, util.List[OrderData]], l: Long): OrderResult = {
    OrderResult(map.get("first").iterator().next().orderId, "fail")
  }
}
