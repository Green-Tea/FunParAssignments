import scala.annotation.tailrec

object Roman extends App {
  // TODO: implement this
  def toRoman(n: Int): String = {

    @tailrec
    def helper(n: Int, listRoman: List[String], listDec: List[Int], result: String): String = {
      if n == 0 then result
      else if n>= listDec.head then helper(n-listDec.head, listRoman, listDec, result + listRoman.head)
      else helper(n, listRoman.tail, listDec.tail, result)
    }
    
    if n<1 || n>=4000 then "Out of bounds"
    else helper(n,
      List("M","DM","DC","D","CD","C","XC","LX","L","XL","X","IX","VI","V","IV","I"),
      List(1000,900,600,500,400,100,90,60,50,40,10,9,6,5,4,1), "")
  }
}
