package com.john.project.application

import com.john.project.common.TApplication
import com.john.project.controller.UniqueViewController

object UniqueViewByBLApplication extends App with TApplication{
  start("unique"){
    val uniqueViewController = new UniqueViewController
    uniqueViewController.getUVByBL("F:\\myFlink0830Tutorial\\input\\UserBehavior.csv")
  }
}
