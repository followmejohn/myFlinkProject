package com.john.project.util

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object FlinkStreamEnv {
  // 可以将数据存储到线程对象中，防止数据冲突
  // ThreadLocal
  private val local = new ThreadLocal[StreamExecutionEnvironment]
  def init(): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    local.set(env)
  }
  def executor(): Unit ={
    local.get().execute()
  }
  def get(): StreamExecutionEnvironment = {
    local.get()
  }
}
