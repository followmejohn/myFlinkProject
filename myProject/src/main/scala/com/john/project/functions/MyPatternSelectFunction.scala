package com.john.project.functions

import java.util

import com.john.project.bean.LoginData
import org.apache.flink.cep.PatternSelectFunction

class MyPatternSelectFunction extends PatternSelectFunction[LoginData, String]{
  override def select(map: util.Map[String, util.List[LoginData]]): String = {
    map.toString
  }
}
