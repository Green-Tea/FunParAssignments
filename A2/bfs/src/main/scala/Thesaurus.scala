object Thesaurus {

  val defaultEncoding = "ISO8859-1"

  def load(filename: String) = ???

  def linkage(thesaurusFile: String): String => String => Option[List[String]] = ???
}
