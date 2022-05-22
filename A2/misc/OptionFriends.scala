import scala.annotation.tailrec

object OptionFriends extends App {

  @tailrec
  def lookup(xs: List[(String, String)], key: String): Option[String] = {
    if xs.isEmpty then None
    else if xs.head._1 == key then Some(xs.head._2)
    else lookup(xs.tail, key)
  }

  def resolve(userIdFromLoginName: String => Option[String],
              majorFromUserId: String => Option[String],
              divisionFromMajor: String => Option[String],
              averageScoreFromDivision: String => Option[Double],
              loginName: String): Double =
    userIdFromLoginName(loginName).flatMap(majorFromUserId).flatMap(divisionFromMajor).flatMap(averageScoreFromDivision).getOrElse(0.0)

}
