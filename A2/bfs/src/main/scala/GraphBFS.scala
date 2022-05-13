object GraphBFS {

  def bfs[V](nbrs: V => Set[V], src: V) = {

    def expand(frontier: Set[V], parent: Map[V, V]) = ???

    def iterate(frontier: Set[V], 
                parent: Map[V, V], 
                distance: Map[V, Int], d: Int
                ): (Map[V, V], Map[V, Int]) =
      if frontier.isEmpty then
        (parent, distance)
      else {
        val (frontier_, parent_) = expand(frontier, parent)
        val distance_ = ???

        iterate(frontier_, parent_, distance_, d + 1)
      }

    iterate(Set(src), Map(src -> src), Map(), 0)
  }

}
