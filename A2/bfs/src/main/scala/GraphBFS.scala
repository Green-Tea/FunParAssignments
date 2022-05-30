object GraphBFS extends App{
//  def nbrs(node: String): Set[String] = {
//    if node.equals("A") then Set("B","C")
//    else if node.equals("B") then Set("A","D")
//    else if node.equals("C") then Set("A","E")
//    else if node.equals("D") then Set("B","F")
//    else if node.equals("E") then Set("C","F")
//    else if node.equals("F") then Set("D", "E")
//    else Set()
//  }

  def bfs[V](nbrs: V => Set[V], src: V): (Map[V, V], Map[V, Int]) = {
    def expand(frontier: Set[V], parent: Map[V, V]) : (Set[V], Map[V, V]) =
      val newFront: Set[V]= frontier.flatMap(nbrs(_).filterNot(parent.contains))
      //find the neighbour of each node in the frontier

      def findNbrs(front: Set[V], result: Map[V, V]): Map[V, V] =
        if front.isEmpty then return result
        //      val newResult = result ++ nbrs(front.head).flatMap(_ => _ -> front.head)
        val newResult: Map[V,V] = result ++ nbrs(front.head).filterNot(parent.contains).foldLeft(Map[V, V]()){(map, V) => map + (V -> front.head)}
        findNbrs(front.tail, newResult)

      val newSugarDaddy: Map[V, V] = parent ++ findNbrs(frontier, Map[V,V]())
      (newFront,newSugarDaddy)

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

//  println(bfs(nbrs,"A"))
}