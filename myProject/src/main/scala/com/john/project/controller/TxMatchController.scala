package com.john.project.controller

import com.john.project.service.TxMatchService

class TxMatchController {
  private val txMatchService = new TxMatchService
  def getTx(dataPath1: String, dataPath2: String)={
    txMatchService.getTx(dataPath1, dataPath2)
  }
}
