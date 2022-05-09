object AllPerm extends App {
  def allPerm(n: Int): List[List[Int]] = {
    if n == 0 then List(List(0))
    else
      if n == 1 then List(List(1))
      else helper(allPerm(n-1), n)
  }

  def helper(list: List[List[Int]], n: Int): List[List[Int]] = {
    if list.size < 1 then Nil
    else
      insertAllPos(list.head, n,0) ::: helper(list.tail, n)
  }

  def insertAllPos(list: List[Int], n: Int, pos: Int): List[List[Int]] = {
    if pos > list.size then Nil
    else insertAt(list, pos, n) :: insertAllPos(list, n, pos+1)
  }

  def insertAt(list: List[Int], i: Int, value: Int): List[Int] = list match {
    case head :: tail if i > 0 => head :: insertAt(tail, i-1, value)
    case _ => value :: list
  }
}
