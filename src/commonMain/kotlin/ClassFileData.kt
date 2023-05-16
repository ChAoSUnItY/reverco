import kotlinx.serialization.Serializable

@Serializable
data class ClassFileData(val fileName: String, val bytes: List<Byte>)
