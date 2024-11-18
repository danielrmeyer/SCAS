import com.example.symengine.cwrapper_h
import java.lang.foreign.{Arena, MemorySegment}
import java.nio.charset.StandardCharsets

class Val(value: Int) extends Expr(cwrapper_h.basic_new_heap()):
  init()

  private def init(): Unit =
    cwrapper_h.integer_set_si(ptr, value) // Set the integer value

  override def toString: String = value.toString
end Val

object Val:
  def apply(value: Int): Val = new Val(value)
end Val
