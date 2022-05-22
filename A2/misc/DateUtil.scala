import javax.xml.transform.Result
import scala.annotation.tailrec

object DateUtil extends App {
  type Date = (Int, Int, Int)

  def isOlder(x: Date, y: Date): Boolean = {
    if x._3 < y._3 then true
    else if x._2 < y._2 then true
    else if x._1 < y._1 then true
    else false
  }

  def numberInMonth(xs: List[Date], month: Int): Int = xs.count(_._2 == month)

  def numberInMonths(xs: List[Date], months: List[Int]): Int = {
    if months.isEmpty then 0
    else numberInMonth(xs, months.head) + numberInMonths(xs, months.tail)
  }

  def datesInMonth(xs: List[Date], month: Int): List[Date] = xs.filter(_._2 == month)

  def datesInMonths(xs: List[Date], months: List[Int]): List[Date] = {
    if months.isEmpty then Nil
    else datesInMonth(xs, months.head) ::: datesInMonths(xs, months.tail)
  }

  def dateToString(d: Date): String = {
    val months = List("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    months(d._2-1) + "-" +  d._1 + "-" + d._3
  }

  def whatMonth(n: Int, yr: Int): Int = ???

  def oldest(dates: List[Date]): Option[Date] = ???

  def isReasonableDate(d: Date): Boolean = ???

//  println(isOlder((21,5,2022), (20,5,2002)))
//  println(numberInMonth(List((1,2,2011), (2,12,2011), (4,2,2011)),3))
//  println(numberInMonths( List((1,2,2011), (2,12,2011), (4,2,2011)) , List(2, 12)))
//  println(datesInMonth( List((1,2,2011), (2,12,2011), (4,2,2011), (2,3,2011), (10,2,2011)), 2))
//  println(datesInMonths( List((1,2,2011), (2,12,2011), (4,2,2011), (2,3,2011), (10,2,2011), (4,8,2011), (2,7,2011), (10,9,2011)), List(2,7)))
//  println(dateToString(12,5,2014))
//  println(whatMonth(61,2016))
//  println(oldest( List((1,1,2001), (51,10,2015), (12,4,2001))))
//  println(isReasonableDate( (29,2,4) ))
}
