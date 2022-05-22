object UseLib extends App {
  def onlyBeginsWithLower(xs: Vector[String]): Vector[String] = {
    val a = xs.filter(_.nonEmpty)
    a.filter(_.charAt(0).isLower)
  }

  def longestString(xs: Vector[String]): Option[String] = if xs.isEmpty then None else Option(xs.maxBy(word => word.length))

  def longestLowercase(xs: Vector[String]): Option[String] = {
    if xs.isEmpty then None
    else Some(xs.filter(i => i.charAt(0).isLower).maxBy(x=>x.length))
  }

}
