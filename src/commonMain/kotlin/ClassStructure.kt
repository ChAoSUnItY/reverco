import kotlinx.serialization.Serializable

@Serializable
data class ClassStructure(
    val magicNumberSpan: Span = Span(0, 4),
    val minorVersionSpan: Span = Span(4, 2),
    val minorVersionRaw: List<Byte>,
    val minorVersion: UShort = minorVersionRaw.toInt().toUShort(),
    val majorVersionSpan: Span = Span(6, 2),
    val majorVersionRaw: List<Byte>,
    val majorVersion: UShort = majorVersionRaw.toInt().toUShort(),
    val constantPoolLengthSpan: Span = Span(8, 2),
    val constantPoolLengthRaw: List<Byte>,
    val constantPoolLength: UShort = constantPoolLengthRaw.toInt().toUShort(),
) {
    constructor(minorVersion: List<Byte>, majorVersion: List<Byte>, constantPoolLength: List<Byte>) : this(
        minorVersionRaw = minorVersion,
        majorVersionRaw = majorVersion,
        constantPoolLengthRaw = constantPoolLength
    )
}
