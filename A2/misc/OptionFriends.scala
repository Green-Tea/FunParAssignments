object OptionFriends extends App {
  def lookup(xs: List[(String, String)], key: String): Option[String] = ???

  def resolve(userIdFromLoginName: String => Option[String],
              majorFromUserId: String => Option[String],
              divisionFromMajor: String => Option[String],
              averageScoreFromDivision: String => Option[Double],
              loginName: String): Double = ???
}
