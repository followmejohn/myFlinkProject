package com.john

import org.apache.flink.streaming.api.functions.source.SourceFunction

import scala.util.Random

class MySource extends SourceFunction[MyAnimalAge]{
  var flag = true
  override def run(ctx: SourceFunction.SourceContext[MyAnimalAge]): Unit = {
    while (flag){

      ctx.collect(MyAnimalAge(('a'+ Random.nextInt(26)).toChar + "",1 + Random.nextInt(99)))
      Thread.sleep(200)
    }
  }

  override def cancel(): Unit = {
    flag = false
  }
}
  case class MyAnimalAge(name: String, age: Int)

