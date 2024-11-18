val x = Var("x") // Create a symbol
val expr = (x + 1) ^ 2 // Create an expression
val expandedExpr = expr.expand() // Expand the expression
println(expandedExpr) // Should produce simplified output

println(expandedExpr.diff(x))
