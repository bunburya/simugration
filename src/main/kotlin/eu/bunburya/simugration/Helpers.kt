package eu.bunburya.simugration

import kotlin.math.pow
import kotlin.math.round

fun ClosedFloatingPointRange<Double>.random(): Double = (Math.random() * (endInclusive - start)) + start
fun ClosedFloatingPointRange<Double>.toIntRange(): IntRange = start.toInt()..endInclusive.toInt()
operator fun ClosedFloatingPointRange<Double>.times(n: Double): ClosedFloatingPointRange<Double> = (start*n)..(endInclusive*n)
fun IntRange.toDoubleRange(): ClosedFloatingPointRange<Double> = first.toDouble()..last.toDouble()
operator fun IntRange.times(n: Int): IntRange = (start*n)..(endInclusive*n)

fun Double.roundTo(n: Int): Double {
    var multiplier = 10.0.pow(n)
    return round(this * multiplier) / multiplier
}

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