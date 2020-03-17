package com.john.project.functions

import com.john.project.util.MyBloomFilter
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import redis.clients.jedis.Jedis

class UniqueViewByBLFunction extends ProcessWindowFunction[(String, Long), (String, Long), String, TimeWindow]{
  private var jedis : Jedis = _
  override def open(parameters: Configuration): Unit = {
    jedis = new Jedis("hadoop203", 6379)
  }
  override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[(String, Long)]): Unit = {
    val storeKey= context.window.getEnd.toString
    var count = 0L
    //如果count已存在，则取出其值
    if(jedis.hget("uvcount", storeKey) != null){
      count = jedis.hget("uvcount", storeKey).toLong
    }
    val str: String = elements.iterator.next()._2.toString
    val offSet: Long = MyBloomFilter.offSet(str,59)
    if(jedis.getbit(storeKey, offSet)){
      //该key可能存在
      out.collect(storeKey, count)
    }else{
      //该key一定不存在
      // 在redis中更新用户ID所在位图的状态
      jedis.setbit(storeKey, offSet, true)
      // 在redis中统计uvcount
      jedis.hset("uvcount", storeKey, (count + 1).toString)
      out.collect(storeKey, count + 1)
    }
  }
}
