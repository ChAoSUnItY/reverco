import kotlinx.serialization.Serializable

@Serializable
data class ClassFileData(val fileName: String, val bytes: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ClassFileData

        if (fileName != other.fileName) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
