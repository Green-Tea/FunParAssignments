object Aggregate extends App {
  // TODO: implement these functions for real!
  def myMin(p: Double, q: Double, r: Double): Double = Math.min(p, Math.min(q,r))
  def myMean(p: Double, q: Double, r: Double): Double = (p+q+r)/3
  def myMed(p: Double, q: Double, r: Double): Double = Math.min(Math.max(p,q), Math.min(Math.max(q,r), Math.max(p,r)))

}
