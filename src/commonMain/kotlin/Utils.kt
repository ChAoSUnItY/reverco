fun List<Byte>.toInt(): Int =
    ((this[0].toInt() and 0xFF) shl 24) or
            ((this[1].toInt() and 0xFF) shl 16) or
            ((this[2].toInt() and 0xFF) shl 8) or
            ((this[3].toInt() and 0xFF) shl 0)

fun Int.toBytes(): List<Byte> = List(4) { i ->
    (this ushr (i * 8)).toByte()
}
