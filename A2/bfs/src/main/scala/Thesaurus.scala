import scala.annotation.tailrec

object Thesaurus extends App {

  val defaultEncoding = "ISO8859-1"

  def load(filename: String) = {
    val source = scala.io.Source.fromFile(filename)
    try {
      val lines = source.getLines()
      val nextLine = lines.next()

      @tailrec
      def toDict(current: Iterator[String], storage: Map[String, Set[String]]): Map[String, Set[String]] = {
        if current.isEmpty then storage
        else
          val line = current.next()
          val storeKey = line.trim.split('|').head
          val len = line.substring(line.lastIndexOf("|")).tail.toInt
          val storeVal = synonym(lines, len, Set())
          toDict(current, storage + (storeKey -> storeVal))
      }

      @tailrec
      def synonym(current: Iterator[String], lines: Int, value: Set[String]): Set[String] = {
        if current.nonEmpty && lines > 0 then
          val line = current.next()
          val newVal = value ++ line.trim.split('|').tail
          synonym(current, lines - 1, newVal)
        else value
      }

      toDict(lines, Map[String, Set[String]]())
    } finally {source.close}
  }

  def linkage(thesaurusFile: String): String => String => Option[List[String]] = {
    val data = load(thesaurusFile)
    val nbrs = (storeKey: String) => data.getOrElse(storeKey, Nil).toSet
    val (parent, distance) = GraphBFS.bfs(nbrs, "clear")

    def parents(parent: Map[String, String], target: String, result: List[String]): Option[List[String]] = {
      if parent.isEmpty then Nil
      val nextWord = parent.get(target)
      if nextWord.isEmpty then None
      if nextWord.get.equals(target) then Some(result.reverse)
      else parents(parent, nextWord.get, result :+ nextWord.get)
    }

    val ans = (storeKey: String) => find => parents(GraphBFS.bfs(nbrs, storeKey)._1, find, List(find))
    ans
  }


}
