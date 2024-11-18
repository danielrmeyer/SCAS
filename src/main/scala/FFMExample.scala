import java.lang.foreign._
import java.lang.foreign.ValueLayout._
import java.lang.invoke.MethodHandles
import java.nio.charset.StandardCharsets

object FFMExample {
  def main(args: Array[String]): Unit = {
    // The string we want to measure the length of
    val str = "Hello, world!"

    // Create a native memory arena to manage the memory allocation
    val arena = Arena.ofConfined()
    try {
      // Convert the Scala string to a UTF-8 encoded byte array
      val bytes = str.getBytes(StandardCharsets.UTF_8)

      // Allocate memory for the UTF-8 string, including space for the null terminator
      val cStr = arena.allocate(bytes.length + 1) // +1 for null terminator

      // Create a temporary segment for the byte array
      val tempSegment = MemorySegment.ofArray(bytes)

      // Copy the UTF-8 bytes into the allocated memory
      cStr.copyFrom(tempSegment)
      cStr.set(JAVA_BYTE, bytes.length, 0.toByte) // Set null terminator

      // Load the strlen function from the standard library
      val linker = Linker.nativeLinker()
      val strlenSymbol = linker.defaultLookup().find("strlen").orElse(null)

      // Ensure strlen function was found
      if (strlenSymbol == null) {
        throw new RuntimeException("Could not find strlen function in the C standard library.")
      }

      // Define the function descriptor for strlen (accepts a pointer and returns a long)
      val strlenDescriptor = FunctionDescriptor.of(JAVA_LONG, ADDRESS)

      // Link the strlen function to a MethodHandle
      val strlenHandle = linker.downcallHandle(strlenSymbol, strlenDescriptor)

      // Call strlen using the MethodHandle
      val length = strlenHandle.invoke(cStr).asInstanceOf[Long]

      // Print the result
      println(s"The length of the string is: $length")
    } finally {
      // Free the memory allocated for the string when we're done
      arena.close()
    }
  }
}
