package com.john.project.application

import com.john.project.common.TApplication
import com.john.project.controller.AdvertClickAnalysisController

object AdvertClickAnalysisApplication extends App with TApplication {
  start("ad"){
    val advertClickAnalysisController = new AdvertClickAnalysisController
    advertClickAnalysisController.getAdvertClick("F:\\myFlink0830Tutorial\\input\\AdClickLog.csv").print
  }
}
