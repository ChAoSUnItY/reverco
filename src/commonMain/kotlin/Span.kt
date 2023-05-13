import kotlinx.serialization.Serializable

@Serializable
data class Span(val startPos: Int, val length: Int) {
}
