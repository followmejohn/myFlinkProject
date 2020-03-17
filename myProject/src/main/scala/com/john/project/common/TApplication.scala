package com.john.project.common

import com.john.project.util.FlinkStreamEnv


trait TApplication {
  def start(back: String )(op: => Unit)={
    try{
    FlinkStreamEnv.init()
    op
    FlinkStreamEnv.executor()
    }catch {
      case e => e.printStackTrace()
    }
  }
  def close(op: => Unit): Unit ={
    op
  }
}
