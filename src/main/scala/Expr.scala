import com.example.symengine.cwrapper_h
import java.lang.foreign.{Arena, MemorySegment}
import java.nio.charset.StandardCharsets

class Expr protected (protected val ptr: MemorySegment):
  def +(other: Expr): Expr = binaryOp(cwrapper_h.basic_add, other)
  def -(other: Expr): Expr = binaryOp(cwrapper_h.basic_sub, other)
  def *(other: Expr): Expr = binaryOp(cwrapper_h.basic_mul, other)
  def /(other: Expr): Expr = binaryOp(cwrapper_h.basic_div, other)
  def ^(exponent: Int): Expr =
    val expSegment = cwrapper_h.basic_new_heap()
    cwrapper_h.integer_set_si(expSegment, exponent)
    val exp = Expr.fromSegment(expSegment)
    binaryOp(cwrapper_h.basic_pow, exp)
  def unary_- : Expr = unaryOp(cwrapper_h.basic_neg)

  def sqrt(): Expr = unaryOp(cwrapper_h.basic_sqrt)
  def cbrt(): Expr = unaryOp(cwrapper_h.basic_cbrt)
  def abs(): Expr = unaryOp(cwrapper_h.basic_abs)
  def expand(): Expr = unaryOp(cwrapper_h.basic_expand)
//  def expand(): Expr =
//    val expanded = cwrapper_h.basic_new_heap()
//    try
//      cwrapper_h.basic_expand(expanded, ptr) // Expand the expression
//      Expr.fromSegment(expanded)
//    catch case e: Throwable =>
//      cwrapper_h.basic_free_heap(expanded) // Free memory if an error occurs
//      throw e

  def diff(symbol: Expr): Expr =
    val result = cwrapper_h.basic_new_heap()
    cwrapper_h.basic_diff(result, ptr, symbol.ptr)
    Expr.fromSegment(result)

  def evalf(precision: Int = 15): Expr =
    val result = cwrapper_h.basic_new_heap()
    cwrapper_h.basic_evalf(result, ptr, precision, 1) // Real evaluation
    Expr.fromSegment(result)

  def ==(other: Expr): Boolean = cwrapper_h.basic_eq(ptr, other.ptr) == 1
  def !=(other: Expr): Boolean = cwrapper_h.basic_neq(ptr, other.ptr) == 1

  private def binaryOp(op: (MemorySegment, MemorySegment, MemorySegment) => Unit, other: Expr): Expr =
    val result = cwrapper_h.basic_new_heap()
    op(result, this.ptr, other.ptr)
    Expr.fromSegment(result)

  private def unaryOp(op: (MemorySegment, MemorySegment) => Unit): Expr =
    val result = cwrapper_h.basic_new_heap()
    op(result, ptr)
    Expr.fromSegment(result)

  override def toString: String =
    val strSegment = cwrapper_h.basic_str(ptr)
    try
      val buffer = strSegment.asSlice(0, 1024).asByteBuffer()
      //val buffer = strSegment.asSlice(0, 2048).asByteBuffer()
      val length = (0 until buffer.limit()).takeWhile(buffer.get(_) != 0).length
      val result = StandardCharsets.UTF_8.decode(buffer.limit(length)).toString
      result
    finally
      cwrapper_h.basic_str_free(strSegment)
end Expr

object Expr:
  def fromSegment(segment: MemorySegment): Expr = new Expr(segment)
  def apply(value: Int): Expr =
    val ptr = cwrapper_h.basic_new_heap()
    cwrapper_h.integer_set_si(ptr, value)
    Expr.fromSegment(ptr)

  given Conversion[Int, Expr] with
    def apply(value: Int): Expr = Expr(value)
end Expr
