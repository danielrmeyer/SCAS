
def expandTest(): Unit =
  println("Expand Test")
  val x = Var("x") // Create a symbol
  val expr = (x + 1) ^ 2 // Create an expression
  val expandedExpr = expr.expand() // Expand the expression
  println(expandedExpr) // Should produce simplified output


//  val y = Var("y")
//  val expr1 = (x + y + Val(2).sqrt()) ^ 6
//  println(expr1)
//  println(
//    expr1.expand()
//  )

def differentiationTest(): Unit =
  println("Differentiation Test")
  val x = Var("x")
  val diffExpr = (x ^ 3).diff(x)
  println(diffExpr) // Should print "3 * x**2"

def basicArithmeticTest(): Unit =
  println("Basic Arithmetic Test")
  val x = Var("x")
  val y = Val(2)
  val expr = x + y
  println(expr) // Should print "(x + 2) * (x - 2)"


@main def runExample(): Unit =
  val x = Var("x") // Create a symbol
  val expr = (x + 1) ^ 2 // Create an expression
  val expandedExpr = expr.expand() // Expand the expression
  println(expandedExpr) // Should produce simplified output
end runExample
