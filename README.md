# SCAS (Scala Computer Algebra System)

* The goal of this project is to create a computer algebra system for Scala 3
* First goal is to wrap the symengine c++ library using the new java ffm library (project Panama).
* Second goal is to implement the simplify method in Scala 3 once all the methods in cwrapper.h have been exposed.

# Example

```scala
val x = Var("x") // Create a symbol
val expr = (x + 1) ^ 2 // Create an expression
val expandedExpr = expr.expand() // Expand the expression
println(expandedExpr) // 1 + 2*x + x**2

println(expandedExpr.diff(x)) // 2 + 2*x
```





