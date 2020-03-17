package com.john.project.bean

// 服务器日志数据对象
case class ApacheLogEvent(ip:String, userId:String, eventTime:Long, method:String, url:String)

