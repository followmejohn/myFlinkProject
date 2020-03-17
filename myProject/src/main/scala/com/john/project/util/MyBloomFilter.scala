package com.john.project.util

import java.util.Date
import java.text.SimpleDateFormat

object MyBloomFilter {
  // 位图容量
  val cap = 1 << 29
  def offSet(s: String, seed: Int)={
    var hashResult = 0L
    for(c <- s){
    hashResult = hashResult * seed + c.toLong
    }
    hashResult & (cap - 1)
  }

}
