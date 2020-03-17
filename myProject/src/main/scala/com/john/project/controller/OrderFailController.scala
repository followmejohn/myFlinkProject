package com.john.project.controller

import com.john.project.service.OrderFailService

class OrderFailController {
  private val orderFailService = new OrderFailService
  def getOrderFail(dataPath: String)={
    orderFailService.getOrderFail(dataPath)
  }
}
