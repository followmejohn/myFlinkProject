package com.john.project.functions

import java.util

import com.john.project.bean.{OrderData, OrderEvent, OrderResult}
import org.apache.flink.cep.PatternSelectFunction

class OrderSelectFunction extends PatternSelectFunction[OrderData, OrderResult]{
  override def select(map: util.Map[String, util.List[OrderData]]): OrderResult = {
    OrderResult(map.get("first").iterator().next().orderId, "success")
  }
}

