object Spaghetti extends App {

  def spaghetti: LazyList[String] = spagHelper("1")

  def helper(xs: String, oldNum: Char, freq: Int): String = xs match {
    case "" => freq+oldNum.toString
    case _ =>
      if xs.head == oldNum then helper(xs.tail, xs.head, freq + 1)
      else freq + oldNum.toString + helper(xs.tail, xs.head, 1)
  }

  def readAloud(xs: String): String = {
    if xs.isEmpty then ""
    else helper(xs.tail, xs.head, 1)
  }

  def spagHelper(str: String): LazyList[String] = str #:: spagHelper(readAloud(str))

  def ham: LazyList[String] = helper(1).flatMap(gray)

  def gray(n: Int): List[String] = {
    if (n == 1) {
      List("0", "1")
    } else {
      val original = gray(n - 1)
      val reversed = original.reverse
      original.map("0" + _) ++ reversed.map("1" + _)
    }
  }

  def helper(i: Int): LazyList[Int] = {
    i #:: helper(i+1)
  }
}
