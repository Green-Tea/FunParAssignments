import jdk.internal.net.http.common.Utils.stackTrace
import org.jsoup.Jsoup

import scala.concurrent.{Await, Future, Promise}
import java.net.URL
import scala.concurrent.duration.Duration

object Crawler {

  sealed case class WebStats(
                              // the total number of (unique) files found
                              numFiles: Int,
                              // the total number of (unique) file extensions (.jpg is different from .jpeg)
                              numExts: Int,
                              // a map storing the total number of files for each extension.
                              extCounts: Map[String, Int],
                              // the total number of words in all html files combined, excluding
                              // all html tags, attributes and html comments.
                              totalWordCount: Long
                            )

  var visited: Set[String] = Set()
  var extSet: Set[String] = Set()
  var extCount: Map[String, Int] = Map()
  var wordCounter: Int = 0

  def epicFunction(basePath: String, base: URL): Future[Set[String]] = {
    import scala.jdk.CollectionConverters.*

    val futr = Promise[Set[String]]
    try {
      var link: String = basePath.toString

      visited += link
      if link.split('/').takeRight(1).mkString.contains('.') then extSet += link.substring(link.lastIndexOf("."), link.length)

      var frontier: Set[String] = Set()

      val doc = Jsoup.connect(link).ignoreContentType(true).ignoreHttpErrors(true).get()
      val allLinks = doc.select("a[href]")
      val img = doc.select("img[src]")
      val imgLink = img.asScala.map(_.attr("abs:src"))
      val imp = doc.select("link[href]")
      val impLink = imp.asScala.map(_.attr("abs:href"))
      val script = doc.select("script[src]")
      val scriptLink = script.asScala.map(_.attr("abs:src"))
      val linkk = allLinks.asScala.map(_.attr("abs:href"))
      val savelink = linkk ++ imgLink ++ impLink ++ scriptLink

      wordCounter += allLinks.text().trim.split("\\s+").filter(_.nonEmpty).map(_.replaceAll("[^A-Za-z0-9]", "")).toList.size

      wordCounter += img.text().trim.split("\\s+").filter(_.nonEmpty).map(_.replaceAll("[^A-Za-z0-9]", "")).toList.size

      wordCounter += script.text().trim.split("\\s+").filter(_.nonEmpty).map(_.replaceAll("[^A-Za-z0-9]", "")).toList.size

      wordCounter += imp.text().trim.split("\\s+").filter(_.nonEmpty).map(_.replaceAll("[^A-Za-z0-9]", "")).toList.size

      savelink.toSet.foreach {
        i => {
          if i.split('/').takeRight(1).mkString.contains('#') then frontier += i.substring(0, i.lastIndexOf("#"))
          else if i.split('/').takeRight(1).mkString.contains('?') then frontier += i.substring(0, i.lastIndexOf("?"))
          else if i.split('/').takeRight(1).mkString.contains('\\') then frontier += i.substring(0, i.lastIndexOf("\\"))
          else if i.charAt(i.length - 1).equals('/') then {
            val nexti = i + "index.html"
            frontier += nexti
          }
          else frontier += i
        }
      }
      futr.success(frontier)
    } catch {
      case e: Throwable => println(stackTrace(e))
    }
    futr.future
  }

  def expand(frontier: Set[String], host: URL): Set[String] = {
    frontier.flatMap {
      x => {
        if x.contains(host.getHost) && !visited.contains(x) then Await.result(epicFunction(x, host) , Duration.Inf)
        else Set.empty
      }
    }
  }

  def bfs(src: String, host: URL) = {
    var frontier = Await.result(epicFunction(src, host), Duration.Inf)
    while (frontier.nonEmpty) {
      val frontier_ : Set[String] = expand(frontier, host)
      frontier = frontier_
    }
  }


  def crawlForStats(basePath: String): WebStats = {
    val res = bfs(basePath, URL(basePath))
    extSet.map{
      extension => extCount += (extension, visited.count(_.contains(extension)))
    }
    WebStats(visited.size, extSet.size, extCount, wordCounter)
  }

  def main(args: Array[String]) = {
    val sampleBasePath: String = "https://cs.muic.mahidol.ac.th/courses/ooc/api/"
    println(crawlForStats(sampleBasePath))
  }
}
