package com.john

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
//前后两个温度差超过1.7就报警
object Flink01_KeyedStateExample1 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val sourceStream: DataStream[SensorReading] = env.addSource(new SensorSource)
    sourceStream.keyBy(_.id)
      .flatMap(new TemperatureAlertFunction(1.7))
      .print
    env.execute()
  }

  class TemperatureAlertFunction(val threshold: Double) extends RichFlatMapFunction[SensorReading,(String, Double, Double)]{
    var lastTp: ValueState[Double] = _
    override def open(parameters: Configuration): Unit = {
      lastTp = getRuntimeContext.getState(new ValueStateDescriptor[Double]("lastTp",classOf[Double]))
    }
    override def flatMap(value: SensorReading, out: Collector[(String, Double, Double)]): Unit = {
      if((value.temperature - lastTp.value()).abs > threshold){
        out.collect(value.id,lastTp.value(),value.temperature)
      }
      lastTp.update(value.temperature)
    }
  }

}
