package com.john.project.application

import com.john.project.common.TApplication
import com.john.project.controller.TxMatchController

object TxMatchApplication extends App with TApplication{
  start("tx"){
    val txMatchController = new TxMatchController
    txMatchController.getTx("F:\\myFlink0830Tutorial\\input\\OrderLog.csv", "F:\\myFlink0830Tutorial\\input\\ReceiptLog.csv")
      .print()
  }
}
