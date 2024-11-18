import com.example.symengine.cwrapper_h
import java.lang.foreign.{Arena, MemorySegment}
import java.nio.charset.StandardCharsets

class Var(name: String) extends Expr(cwrapper_h.basic_new_heap()):
  private val segment = Arena.ofConfined()

  init()

  private def init(): Unit =
    val nameBytes = name.getBytes(StandardCharsets.UTF_8)
    val nameSegment = segment.allocate(nameBytes.length)
    nameSegment.asByteBuffer().put(nameBytes)
    cwrapper_h.symbol_set(ptr, nameSegment)

  override def toString: String = name
end Var

object Var:
  def apply(name: String): Var = new Var(name)
end Var
