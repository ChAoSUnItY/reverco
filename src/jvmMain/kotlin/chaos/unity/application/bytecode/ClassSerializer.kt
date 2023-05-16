package chaos.unity.application.bytecode

import ClassStructure
import org.apache.bcel.classfile.ClassParser
import toBytes
import java.io.ByteArrayInputStream

fun serializeClass(className: String, bytes: ByteArray): ClassStructure? {
    val classParser = ClassParser(ByteArrayInputStream(bytes), className)

    try {
        println(classParser)
        
        val classFile = classParser.parse()

        println(classFile)
        
        return ClassStructure(
            classFile.minor.toBytes(),
            classFile.major.toBytes(),
            classFile.constantPool.length.toBytes(),
        )
    } catch (e: Exception) {
        e.printStackTrace()

        return null
    }
}
