package com.john

import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object KeyedProcessFunctionExample {
  // KeyedProcessFunction只能操作KeyedStream
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val alert= env.addSource(new SensorSource)
      .keyBy(_.id)
      .process(new TimeIncreaseAlertFunction)
    alert.print
    env.execute()
  }

  class TimeIncreaseAlertFunction extends KeyedProcessFunction[String,SensorReading,String]{
    // 保存上一个传感器温度值
    // 惰性赋值
    // 只会初始化一次，当程序挂掉，再重启的时候，会调用getState方法看一下这个状态变量存在不存在，如果存在，就不初始化
    // 默认值为 0.0
    lazy val lastTemp = getRuntimeContext.getState(
      new ValueStateDescriptor[Double]("lastTemp",Types.of[Double])
    )
    // 默认值为 0L
    lazy val currentTime = getRuntimeContext.getState(
      new ValueStateDescriptor[Long]("currentTime",Types.of[Long])
    )

    override def processElement(value: SensorReading,
                                // #是类型投影的意思，用来访问内部类
                                context: KeyedProcessFunction[String, SensorReading, String]#Context,
                                collector: Collector[String]): Unit = {
      // ValueState的读取使用`.value()`方法
      val prevTemp: Double = lastTemp.value()
      // ValueState的更新使用`.update()`方法
      lastTemp.update(value.temperature)
      val curTimerTimeStamp: Long = currentTime.value()
      if(prevTemp == 0.0 || value.temperature < prevTemp){
        // 温度下降，或者value是第一个温度读数，删除定时器
        context.timerService().deleteProcessingTimeTimer(curTimerTimeStamp)
        // ValueState清空操作使用`.clear()`
        currentTime.clear()
      }else if(value.temperature > prevTemp && curTimerTimeStamp == 0){
        // 当前机器时间 1s 之后的时间戳
        val timeTs: Long = context.timerService().currentProcessingTime() + 1000
        // 在时间戳 timerTs 注册一个定时事件
        context.timerService().registerProcessingTimeTimer(timeTs)
        currentTime.update(timeTs)
      }
    }
    // onTimer 向下发送数据
    override def onTimer(timestamp: Long,
                         ctx: KeyedProcessFunction[String, SensorReading, String]#OnTimerContext,
                         out: Collector[String]): Unit = {
    out.collect("传感器id为：" + ctx.getCurrentKey + "的传感器温度已连续1s上升了")
      // 别忘了清空状态变量
    currentTime.clear()
    }
  }

}
