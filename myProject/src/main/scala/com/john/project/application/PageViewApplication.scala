package com.john.project.application

import com.john.project.common.TApplication
import com.john.project.controller.PageViewController

object PageViewApplication extends App with TApplication{
  start ("sum"){
    val pageViewController = new PageViewController
    pageViewController.getPV("F:\\myFlink0830Tutorial\\input\\UserBehavior.csv").print()
  }
  close()
}
