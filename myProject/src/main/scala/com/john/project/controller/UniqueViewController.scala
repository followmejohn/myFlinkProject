package com.john.project.controller

import com.john.project.service.UniqueViewService

class UniqueViewController {
  private val uniqueViewService = new UniqueViewService
  def getUV(dataPath: String) ={
    uniqueViewService.getUV(dataPath)
  }
  def getUVByBL(dataPath: String) ={
    uniqueViewService.getUVByBL(dataPath)
  }
}
