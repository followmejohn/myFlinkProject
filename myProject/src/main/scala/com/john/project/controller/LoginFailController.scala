package com.john.project.controller

import com.john.project.service.LoginFailService

class LoginFailController {
  private val loginFailService = new LoginFailService
  def getLoginFail(num: Int, dataPath: String)={
    loginFailService.getLoginFail(num, dataPath)
  }
}
