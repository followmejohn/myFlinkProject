package com.john.project

object SortDemo {
  def main(args: Array[String]): Unit = {
    val list = List(4,1,5,7,3)
    val list2 : List[Int] = List()
    println(quickSort(list2))
//    println(haha(list2))
  }
  def quickSort(list: List[Int]): List[Int] = list match {
    case Nil => Nil
    case List() => List()
    case head :: tail =>
      val (left, right) = tail.partition(_ < head)
      quickSort(left) ::: head :: quickSort(right)
  }
  def haha(list: List[Int])= list match {
    case Nil =>
      println("nil")
      Nil
    case List() =>
      println("list")
      List()
//    case first :: second :: third => first + "-" + second + "-" + third
    case _ => "something others"
  }
}
