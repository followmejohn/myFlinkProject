package com.john.project.application

import com.john.project.common.TApplication
import com.john.project.controller.LoginFailController

object LoginFailApplication extends App with TApplication{
  start("login"){
    val loginFailController = new LoginFailController
    loginFailController.getLoginFail(3, "F:\\myFlink0830Tutorial\\input\\LoginLog.csv").print
  }
}
