import scala.annotation.tailrec

object Happy extends App {
  // TODO: write these functions!
  def sumOfDigitsSquared(n: Int): Int = {
    @tailrec
    def helper(n: Int, accum: Int): Int ={
      if n<10 then accum+(n*n)
      else helper(n/10, accum + ((n%10)*(n%10)))
    }
    helper(n,0)
  }
  @tailrec
  def isHappy(n: Int): Boolean = n match {
    case 1 => true
    case 4 => false
    case _ => isHappy(sumOfDigitsSquared(n))
  }
  
  def kThHappy(k: Int): Int = {
    @tailrec
    def helper(k: Int, n: Int, counter: Int): Int = {
      if counter == k then n-1
      else if isHappy(n) then helper(k, n+1,counter+1)
      else helper(k, n+1, counter)
    }

    helper(k,1,0)
  }
 }
