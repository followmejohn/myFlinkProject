package com.john.project.controller

import com.john.project.service.HotPageRankService

class HotPageRankController {
  private val hotPageRankService = new HotPageRankService
  def getHotPageRank(num: Int, dataPath: String)= {
    hotPageRankService.getHotPageRank(num, dataPath)
  }
}
