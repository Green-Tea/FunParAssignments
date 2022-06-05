import scala.annotation.tailrec

object GraphBFS extends App{
  
  def bfs[V](nbrs: V => Set[V], src: V): (Map[V, V], Map[V, Int]) = {
    def expand(frontier: Set[V], parent: Map[V, V]) : (Set[V], Map[V, V]) =
      val newFront: Set[V]= frontier.flatMap(nbrs(_).filterNot(parent.contains))

      @tailrec
      def search(front: Set[V], result: Map[V, V]): Map[V, V] =
        if front.isEmpty then return result
        val newResult: Map[V,V] = result ++ nbrs(front.head).filterNot(parent.contains).foldLeft(Map[V, V]()){(map, V) => map + (V -> front.head)}
        search(front.tail, newResult)

      val newParent: Map[V, V] = parent ++ search(frontier, Map[V,V]())
      (newFront,newParent)

    @tailrec
    def iterate(frontier: Set[V],
                parent: Map[V, V],
                distance: Map[V, Int], d: Int //d = lvl
               ): (Map[V, V], Map[V, Int]) =
      if frontier.isEmpty then
        (parent, distance)
      else {
        val (frontier_, parent_) = expand(frontier, parent)
        val distance_ = distance ++ frontier.foldLeft(Map[V, Int]()){(map, V) => map + (V -> d)}
        iterate(frontier_, parent_, distance_, d + 1)
      }
    iterate(Set(src), Map(src -> src), Map(), 0)
  }
}