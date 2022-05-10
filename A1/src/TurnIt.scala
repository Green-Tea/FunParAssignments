object TurnIt extends App {
  def transpose(A: List[List[Int]]): List[List[Int]] = {

    def getHead(A: List[List[Int]]): List[Int] = A match {
      case Nil => Nil
      case _ => A.head.head :: getHead(A.tail)
    }

    def getTail(A: List[List[Int]]): List[List[Int]] = A match {
      case Nil => Nil
      case _ => A.head.tail :: getTail(A.tail)
    }

    if A.head.isEmpty then Nil
    else getHead(A) :: transpose(getTail(A))
  }

  println(transpose(List(List(1,2,3,4), List(5,6,7,8), List(9,10,11,12))))
}
