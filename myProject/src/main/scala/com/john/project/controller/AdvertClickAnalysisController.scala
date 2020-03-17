package com.john.project.controller

import com.john.project.service.AdvertClickAnalysisService

class AdvertClickAnalysisController {
  private val advertClickAnalysisService = new AdvertClickAnalysisService
  def getAdvertClick(dataPath: String) ={
    advertClickAnalysisService.getAdvertClick(dataPath)
  }
}
