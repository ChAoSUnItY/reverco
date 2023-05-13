package chaos.unity.application.bytecode

import ClassStructure
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode

fun serializeClass(bytes: ByteArray): ClassStructure? {
    val reader = ClassReader(bytes)
    val classNode = ClassNode()

    reader.accept(classNode, 0)

    return ClassStructure()
}
