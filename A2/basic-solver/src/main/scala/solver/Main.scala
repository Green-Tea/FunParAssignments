package solver

object Solver extends App{
  // solves expString == 0 for the variable in varName with an initial guess
  // specified. We'll assume that the given expression has a root.

  def solve(expString: String, varName: String, guess: Double): Double = {
    val ex = Parser(expString).get // <- f
    //f(x) x = guess
    val f: Double => Double = (k : Double) => Process.eval(ex, Map[String, Double](varName -> k ))
    val df = Process.differentiate(ex, varName) //df(x)
    val x: Double => Double = (k: Double) => Process.eval(df, Map[String, Double](varName -> k))

    // TODO: complete the implementation. This will construct the 
    // appropriate functions and call Newton.solve
    def helper(f: Double => Double, x: Double => Double, guess: Double): Double = {
      val newGuess = Newton.solve(f,x,guess).get
      if math.abs(newGuess - guess)< 0.000000000001
      then newGuess
      else helper(f,x,newGuess)
    }
    helper(f,x,guess)
  }

  println(solve("x^2-1","x",2.0))


}
