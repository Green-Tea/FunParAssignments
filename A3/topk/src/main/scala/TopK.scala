import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source

object TopK {

  def countWords(text: String): mutable.Map[String, Int] = {
    val counts = mutable.Map.empty[String, Int].withDefaultValue(0)
    for (raw <- text.split("[ ,!.]+")) {
      val word = raw.toLowerCase
      counts(word) += 1
    }
    counts
  }

  def mergeSum(map1: mutable.Map[String,Int], map2: mutable.Map[String,Int]): mutable.Map[String,Int] = {
    map1 ++ map2.map{ case (k,v) => k -> (v + map1.getOrElse(k,0)) }
  }

  def topKWords(k: Int)(fileSpec: String): Vector[(String, Int)] = {
    implicit val executionContext: ExecutionContext = ExecutionContext.global
    val bufferedSource = Source.fromFile(fileSpec)
    var lines = List("")

    for (line <- bufferedSource.getLines) {
      val l = line.toLowerCase
      lines = l :: lines
    }
    bufferedSource.close
    lines = lines.filter(n => n.nonEmpty)
    val handles = lines.map(line => Future{countWords(line)})
    var allAns = mutable.Map.empty[String, Int].withDefaultValue(0)
    val result = Future.sequence(handles).map(ansList => ansList.map { ans => allAns = mergeSum(allAns, ans)})
    Await.result(result, Duration.Inf)
    val ansFinal = allAns.toVector.sortBy(i => (-i._2,i._1))
    ansFinal.take(k)
  }

  def main(args: Array[String]) = {
    val t = topKWords(10)("/home/seaweed/Documents/GitHub/FunPar/FunParAssignments/A3/topk/src/main/scala/topktest.txt")
    println(t)
  }
}
