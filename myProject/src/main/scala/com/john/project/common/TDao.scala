package com.john.project.common

import com.john.project.util.FlinkStreamEnv
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

trait TDao {
  def readTextFile(dataPath: String): DataStream[String] ={
    FlinkStreamEnv.get().readTextFile(dataPath)
  }
  def fromKafka(topic: String): Unit ={
    val properties = new java.util.Properties()
    properties.setProperty("bootstrap.servers", "hadoop201:9092")
    properties.setProperty("group.id", "consumer-group")
    properties.setProperty("key.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("auto.offset.reset", "latest")
    FlinkStreamEnv.get().addSource(
      new FlinkKafkaConsumer011[String](topic, new SimpleStringSchema(), properties)
    )
  }
}
