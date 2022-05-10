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
}
