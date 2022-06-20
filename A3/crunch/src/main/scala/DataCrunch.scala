import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.language.postfixOps

object DataCrunch {

  trait DataProvider {
    def get(onSuccess: Seq[String] => Unit,
            onFailure: () => Unit): Unit
  }


  object LoremIpsum extends DataProvider {
    override def get(onSuccess: Seq[String] => Unit,
            onFailure: () => Unit): Unit = {
      val lorem =
        """Lorem ipsum dolor sit amet, consectetur adipiscing elit.
        Cras nec sagittis justo. Nullam dignissim ultricies velit a tempus.
        Aenean pharetra semper elit eu luctus. Fusce maximus lacus eget magna
        ultricies, ac suscipit justo lobortis. Nullam pellentesque lectus
        at tellus gravida rhoncus. Nam augue tortor, rutrum et eleifend id,
        luctus ut turpis. Vivamus nec sodales augue.

        Suspendisse non erat diam. Mauris hendrerit neque at sem laoreet
          vehicula. Sed aliquam urna a efficitur tincidunt. In non purus
        tincidunt, molestie velit vulputate, mollis nisl. Pellentesque
        rhoncus molestie bibendum. Etiam sit amet felis a orci fermentum
        tempor. Duis ante lacus, luctus ac scelerisque eget, viverra ut felis."""
      onSuccess(lorem.split("\n"))
    }
  }

  object FailedSample extends DataProvider {
    override def get(onSuccess: Seq[String] => Unit,
                     onFailure: () => Unit): Unit = {
      onFailure()
    }
  }

  // This returns a DataProvider that feeds the "consumer" all the lines from a
  // file indicated by filename.
  def FileSource(filename: String): DataProvider = new DataProvider {
    override def get(onSuccess: Seq[String] => Unit, onFailure: () => Unit): Unit = {
      try {
        val lines = io.Source.fromFile(filename)
          .getLines
          .toVector
        onSuccess(lines)
      }
      catch {
        case _: Throwable => onFailure()
      }
    }
  }

  def dataProviderFuture(dp: DataProvider): Future[Seq[String]] = {
    val ans = Promise[Seq[String]]
    try{
      dp.get(line => {ans.success(line)}, funcOnFailure)
    } catch {
      case ex => ans.failure(ex)
    }
    def funcOnFailure() = println("Failed")
    val fileProvider: DataProvider = FileSource("/loremipsum.txt")
    ans.future
  }

  def splitter(linesFut: Future[Seq[String]]): Future[Seq[String]] = {
    linesFut.map(_.flatMap(x => x.trim.split("\\s+")))
  }

  def tokenCount(lines: Seq[String]): Seq[Int] = lines.map(_.split("\\s+").length)

  def wordCounter(linesFuture: Future[Seq[String]]): Future[Int] = linesFuture.map(tokenCount).map(_.sum)

  def countFreq(words: Seq[String]): Map[String, Int] = {
    val result = Promise[Map[String, Int]]
    val setWord: Set[String] = words.toSet
    val frequency: scala.collection.mutable.Map[String, Int] = scala.collection.mutable.Map.empty
    for(word <- setWord){
      frequency.put(word,words.count(_.equals(word)))

    }
    frequency.toMap
  }

  def highestFreq(linesFut: Future[Seq[String]]): Future[(String, Double)] = {
    val word: Future[Seq[String]] = splitter(linesFut)
    val totalWord: Future[Int] = wordCounter(linesFut)
    val freq: Future[Map[String,Int]] = word.map(countFreq)
    val max: Future[(String,Int)] = freq.map(_.maxBy(_._2))

    val ratio: Future[Double] = max.flatMap{
      up => totalWord.map{
        base => up._2.toDouble/base.toDouble
      }
    }

    val res:(Future[String],Future[Double]) = (max.map(_._1),ratio)
    val finalRes: Future[(String, Double)] = for(
      res1 <- res._1;
      res2 <- res._2
    ) yield (res1,res2)
    finalRes
  }

  def main(args: Array[String]) = {
    // Example of how DataProvider is typically used. Comment out the block of code
    // below so it doesn't print some random garbage.
    def funcOnSuccess(lines: Seq[String]) = lines.foreach(println(_))
    def funcOnFailure() = println("Failed")
    val sampleProvider = LoremIpsum
    sampleProvider.get(funcOnSuccess, funcOnFailure)
  }

}
