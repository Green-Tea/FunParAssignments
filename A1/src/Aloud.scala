import scala.annotation.tailrec

object Aloud extends App {
  // TODO: Implement me
  def readAloud(xs: List[Int]): List[Int] = {

    @tailrec
    def helper(xs: List[Int], num: Int, freq: Int, resultList: List[Int]): List[Int] = xs match{
      case Nil => resultList :+ freq :+ num
      case _ =>
        val head = xs.head
        val tail = xs.tail
        if head == num then helper(tail, head, freq+1, resultList)
        else helper(tail,head,1,resultList :+ freq :+ num)
    }

    if xs.isEmpty then Nil
    else helper(xs.tail, xs.head, 1, List[Int]())

  }
}
