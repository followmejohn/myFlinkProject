package com.john
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
object ProcessWindowFunctionDemo {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val sourceDate: DataStream[SensorReading] = env.addSource(new SensorSource)
    sourceDate.keyBy(_.id)
      .timeWindow(Time.seconds(5))
        .process(new HighAndLowTempProcessFunction)
        .print()
    env.execute()
  }
  case class MinMaxTemp(id: String, min: Double, max: Double, ts: Long)
  class HighAndLowTempProcessFunction extends ProcessWindowFunction[SensorReading,MinMaxTemp,String,TimeWindow]{
    // 当窗口闭合的时候调用，Iterable里面包含了窗口收集的所有元素
    override def process(key: String,
                         context: Context,
                         elements: Iterable[SensorReading],
                         out: Collector[MinMaxTemp]): Unit = {
      val minMax: Iterable[Double] = elements.map(_.temperature)
      val endTime: Long = context.window.getEnd
      out.collect(MinMaxTemp(key, minMax.min, minMax.max, endTime))
    }
  }
}

