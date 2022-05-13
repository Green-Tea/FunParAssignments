object DateUtil extends App {
  type Date = (Int, Int, Int)

  def isOlder(x: Date, y: Date): Boolean = ???

  def numberInMonth(xs: List[Date], month: Int): Int = ???

  def numberInMonths(xs: List[Date], months: List[Int]): Int = ???

  def datesInMonth(xs: List[Date], month: Int): List[Date] = ???

  def datesInMonths(xs: List[Date], months: List[Int]): List[Date] = ???

  def dateToString(d: Date): String = ???

  def whatMonth(n: Int, yr: Int): Int = ???

  def oldest(dates: List[Date]): Option[Date] = ???

  def isReasonableDate(d: Date): Boolean = ???
}
