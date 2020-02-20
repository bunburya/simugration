package eu.bunburya.simugration

fun ClosedFloatingPointRange<Double>.random(): Double = (Math.random() * (endInclusive - start)) + start
fun ClosedFloatingPointRange<Double>.toIntRange(): IntRange = start.toInt()..endInclusive.toInt()

fun normalize(rawVal: Double, rawRange: ClosedFloatingPointRange<Double>,
              normRange: ClosedFloatingPointRange<Double>, clipExtremes: Boolean = false): Double {
    val rawRangeSize = rawRange.endInclusive - rawRange.start
    val normRangeSize = normRange.endInclusive - normRange.start
    var norm0to1 = (rawVal - rawRange.start) / rawRangeSize
    if (clipExtremes && norm0to1 !in 0.0..1.0) {
        if (norm0to1 < 0) norm0to1 = 0.0
        else if (norm0to1 > 1) norm0to1 = 1.0
    }
    return (norm0to1 * normRangeSize) + normRange.start
}

fun normalize(rawVal: Double, rawRange: IntRange,
              normRange: IntRange, clipExtremes: Boolean = false): Double {
    val rawRangeSize = rawRange.endInclusive - rawRange.start
    val normRangeSize = normRange.endInclusive - normRange.start
    var norm0to1 = (rawVal - rawRange.start) / rawRangeSize
    if (clipExtremes && norm0to1 !in 0.0..1.0) {
        if (norm0to1 < 0) norm0to1 = 0.0
        else if (norm0to1 > 1) norm0to1 = 1.0
    }
    return (norm0to1 * normRangeSize) + normRange.start
}