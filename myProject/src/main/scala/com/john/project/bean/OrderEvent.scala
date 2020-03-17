package com.john.project.bean

case class OrderEvent(orderId: String, event: String, txId: String, timestamp: Long)