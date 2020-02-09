package eu.bunburya.simugration

fun ClosedFloatingPointRange<Double>.random(): Double = (Math.random() * (endInclusive - start)) + start
fun ClosedFloatingPointRange<Double>.toIntRange(): IntRange = start.toInt()..endInclusive.toInt()

fun normalize(rawVal: Double, rawRange: ClosedFloatingPointRange<Double>,
              normRange: ClosedFloatingPointRange<Double>): Double {
    val rawRangeSize = rawRange.endInclusive - rawRange.start
    val normRangeSize = normRange.endInclusive - normRange.start
    val norm0to1 = (rawVal - rawRange.start) / rawRangeSize
    return (norm0to1 * normRangeSize) + normRange.start
}

fun normalize(rawVal: Double, rawRange: IntRange,
              normRange: IntRange): Double {
    val rawRangeSize = rawRange.endInclusive - rawRange.start
    val normRangeSize = normRange.endInclusive - normRange.start
    val norm0to1 = (rawVal - rawRange.start) / rawRangeSize
    return (norm0to1 * normRangeSize) + normRange.start
}