package com.john.project.application

import com.john.project.common.TApplication
import com.john.project.controller.HotPageRankController

object HotPageRankApplication extends App with TApplication{
  start("hotPage"){
    val hotPageRankController = new HotPageRankController
      hotPageRankController
        .getHotPageRank(3, "")
        .print

  }
  close()
}
