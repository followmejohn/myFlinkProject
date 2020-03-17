package com.john.project.application

import com.john.project.common.TApplication
import com.john.project.controller.UniqueViewController

object UniqueViewApplication extends App with TApplication{
  start("unique"){
    val uniqueViewController = new UniqueViewController
    uniqueViewController.getUV("F:\\myFlink0830Tutorial\\input\\UserBehavior.csv").print
  }
}
