package com.john.project.application

import com.john.project.bean.OrderResult
import com.john.project.common.TApplication
import com.john.project.controller.OrderFailController
import org.apache.flink.streaming.api.scala._

object OrderFailApplication extends App with TApplication{
  start("order"){
    val orderFailController = new OrderFailController
    val resultStream: DataStream[OrderResult] = orderFailController.getOrderFail("F:\\myFlink0830Tutorial\\input\\OrderLog.csv")
    resultStream.getSideOutput(new OutputTag[OrderResult]("failOrder")).print("failOrder>>>")
    resultStream.print()
  }
}
