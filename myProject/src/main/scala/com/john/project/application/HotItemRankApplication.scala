package com.john.project.application

import com.john.project.common.TApplication
import com.john.project.controller.HotItemRankController

object HotItemRankApplication extends App with TApplication {
  start("hotItem") {
  // TODO 热门商品排行控制器
  val hotItemRankController = new HotItemRankController
  // TODO 获取热门商品排行数据并打印到控制台
  hotItemRankController
    .getHotItemRank(3, "F:\\myFlink0830Tutorial\\input\\UserBehavior.csv")
    .print
  }
  close(

  )
}
