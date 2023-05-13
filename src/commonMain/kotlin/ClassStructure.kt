import kotlinx.serialization.Serializable

@Serializable
data class ClassStructure(
    val magicNumber: Span = Span(0, 4),
    val version: Span = Span(4, 8),
) {
}
