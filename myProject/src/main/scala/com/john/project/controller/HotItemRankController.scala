package com.john.project.controller

import com.john.project.service.HotItemRankService

class HotItemRankController {
  private val hotItemRankService = new HotItemRankService
  def getHotItemRank(num: Int, dataPath: String) = {
    hotItemRankService.getHotItemRank(num, dataPath)
  }

}
