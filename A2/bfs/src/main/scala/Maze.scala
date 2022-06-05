import scala.annotation.tailrec

object Maze extends App {
  case class Index(x: Int, y: Int)

  def solveMaze(maze: Vector[String]): Option[String] = {
    val search: (Index, Index, Map[Index,Set[Index]]) = walk(maze,Index(0,0), Index(0,0), Index(0,0), Map())


    val nbrs: Index => Set[Index] = optimise(search)
    evokeBFS(nbrs,search._1,search._2)
  }

  def optimise(save: (Index, Index, Map[Index,Set[Index]])): Index => Set[Index] = {
    val res = (key: Index) => save._3.getOrElse(key, Nil).toSet

    res
  }

  @tailrec
  def walk(maze: Vector[String], currentIndex: Index, startIndex: Index, endIndex: Index, saveIndex: Map[Index, Set[Index]]): (Index, Index, Map[Index,Set[Index]]) = {
    val nbrs = List[String]("N", "S", "W", "E")
    if currentIndex.y > maze.size-2 then (startIndex, endIndex, saveIndex)
    else if currentIndex.x > maze.head.length-1 then walk(maze, Index(0,currentIndex.y+1), startIndex, endIndex, saveIndex)
    else if maze(currentIndex.y).charAt(currentIndex.x).toString.equals(" ") then
      walk(maze, Index(currentIndex.x+1,currentIndex.y), startIndex, endIndex, saveIndex ++ Map(currentIndex -> findNbrs(maze, currentIndex, nbrs, Set())))
    else if maze(currentIndex.y).charAt(currentIndex.x).toString.equals("s") then
      walk(maze, Index(currentIndex.x+1,currentIndex.y), currentIndex, endIndex, saveIndex ++ Map(currentIndex -> findNbrs(maze, currentIndex, nbrs, Set())))
    else if maze(currentIndex.y).charAt(currentIndex.x).toString.equals("e") then
      walk(maze, Index(currentIndex.x+1,currentIndex.y), startIndex, currentIndex, saveIndex ++ Map(currentIndex -> findNbrs(maze, currentIndex, nbrs, Set())))
    else walk(maze, Index(currentIndex.x+1,currentIndex.y), startIndex, endIndex, saveIndex)
  }

  @tailrec
  def findNbrs(maze: Vector[String], currentIndex: Index, nbrs: List[String], listIndex: Set[Index]): Set[Index] = nbrs.size match {
    case 0 => listIndex
    case 4 => if !maze(currentIndex.y).charAt(currentIndex.x+1).toString.equals("x") then findNbrs(maze, currentIndex, nbrs.tail, listIndex + Index(currentIndex.x+1,currentIndex.y))
    else findNbrs(maze, currentIndex, nbrs.tail, listIndex)
    case 3 => if !maze(currentIndex.y).charAt(currentIndex.x-1).toString.equals("x") then findNbrs(maze, currentIndex, nbrs.tail, listIndex + Index(currentIndex.x-1,currentIndex.y))
    else findNbrs(maze, currentIndex, nbrs.tail, listIndex)
    case 2 => if !maze(currentIndex.y+1).charAt(currentIndex.x).toString.equals("x") then findNbrs(maze, currentIndex, nbrs.tail, listIndex + Index(currentIndex.x,currentIndex.y+1))
    else findNbrs(maze, currentIndex, nbrs.tail, listIndex)
    case 1 => if !maze(currentIndex.y-1).charAt(currentIndex.x).toString.equals("x") then findNbrs(maze, currentIndex, nbrs.tail, listIndex + Index(currentIndex.x,currentIndex.y-1))
    else findNbrs(maze, currentIndex, nbrs.tail, listIndex)
  }

  def evokeBFS(path: Index => Set[Index], start: Index, end: Index): Option[String] = {
    if path(start).isEmpty then None
    else {

      val map = GraphBFS.bfs(path, end)._1


      val pathIndex = start +: mapToList(map, List(), end, start, map)


      Option(bearing(pathIndex.tail, pathIndex.head, ""))
    }
  }


  @tailrec
  def mapToList( map: Map[Index,Index], result: List[Index], start: Index, des: Index, collection: Map[Index,Index]): List[Index] = {
    if map.tail.isEmpty then Nil
    else {
      if des.equals(start)  then result
      else {
        val newWord: Index = collection.getOrElse(des,Index(0,0))
        mapToList(map.tail, result :+ newWord, start, newWord,collection)
      }
    }
  }


  @tailrec
  def bearing(path: List[Index], prevIndex: Index, bear: String): String = {
    if path.isEmpty then bear
    else if prevIndex.x == path.head.x + 1 && prevIndex.y == path.head.y then bearing(path.tail, path.head, bear + "l")
    else if prevIndex.x == path.head.x - 1 && prevIndex.y == path.head.y then bearing(path.tail, path.head, bear + "r")
    else if prevIndex.y == path.head.y - 1 && prevIndex.x == path.head.x then bearing(path.tail, path.head, bear + "d")
    else bearing(path.tail, path.head, bear + "u")
  }
}
