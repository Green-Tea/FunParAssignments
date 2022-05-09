object Zombies extends App {
  def countBad(hs: List[Int]): Int = {
    merge(hs, 0)._2
  }

  def count(l1: List[Int], l2: List[Int], counter: Int): (List[Int], Int) = (l1, l2) match {
    case (_, Nil) => (l1, counter)
    case (Nil, _) => (l2, counter)
    case (leftHead :: leftTail, rightHead :: rightTail) =>
      if leftHead > rightHead then (leftHead :: count(leftTail, l2, counter)._1,count(leftTail, l2, counter)._2)
      else (rightHead :: count(l1, rightTail, counter + l1.length)._1, count(l1, rightTail, counter + l1.length)._2)
  }

  def merge(list: List[Int], counter: Int): (List[Int], Int) = list match {
    case Nil => (list, counter)
    case h::Nil => (list, counter)
    case _ =>
      val (l1, l2) = list.splitAt(list.length/2)
      count(merge(l1, counter)._1, merge(l2, counter)._1, merge(l1, counter)._2 + merge(l2, counter)._2)
  }


}