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

  def whatMonth(n: Int, yr: Int): Int = {
    @tailrec
    def findMonth(n: Int, dayInMonth: List[(Int,Int)]): Int = n match {
      case n if n <= dayInMonth.head._2 => dayInMonth.head._1
      case _ => findMonth(n-dayInMonth.head._2, dayInMonth.tail)
    }
    val LeapYr = List((1,31),(2,29),(3,31),(4,30),(5,31),(6,30),(7,31),(8,31),(9,30),(10,31),(11,30),(12,31))
    val NormalYr = List((1,31),(2,28),(3,31),(4,30),(5,31),(6,30),(7,31),(8,31),(9,30),(10,31),(11,30),(12,31))

    if (yr%4 == 0 || yr%400 ==0)&&(yr%100 != 0) then findMonth(n,LeapYr)
    else findMonth(n,NormalYr)
  }

//  def oldest(dates: List[Date]): Option[Date] = dates match {
//    case Nil => None
//    case _ => dates.foldLeft(dates.head)((x, y) => isOlder(x, y))
//  }

  def isReasonableDate(d: Date): Boolean = d match{
    case d if d._3 < 1 || d._2 < 1 || d._2 > 12 || d._1 < 1 => false
    case d if (d._3%4 == 0 || d._3%400 ==0)&&(d._3%100 != 0) =>
      val daysInMonthLeapYr = List(31,29,31,30,31,30,31,31,30,31,30,31)
      d._1 <= daysInMonthLeapYr(d._2)
    case d if (d._3%4 == 0 || d._3%400 ==0)&&(d._3%100 != 0) =>
      val daysInMonthNormalYr = List(31,28,31,30,31,30,31,31,30,31,30,31)
      d._1 <= daysInMonthNormalYr(d._2)
    case _ => false

  }
//  println(isOlder((21,5,2022), (20,5,2002)))
//  println(numberInMonth(List((1,2,2011), (2,12,2011), (4,2,2011)),3))
//  println(numberInMonths( List((1,2,2011), (2,12,2011), (4,2,2011)) , List(2, 12)))
//  println(datesInMonth( List((1,2,2011), (2,12,2011), (4,2,2011), (2,3,2011), (10,2,2011)), 2))
//  println(datesInMonths( List((1,2,2011), (2,12,2011), (4,2,2011), (2,3,2011), (10,2,2011), (4,8,2011), (2,7,2011), (10,9,2011)), List(2,7)))
//  println(dateToString(12,5,2014))
  println(whatMonth(61,2016))
//  println(oldest( List((1,1,2001), (51,10,2015), (12,4,2001))))
  println(isReasonableDate( (30,2,4) ))
}
