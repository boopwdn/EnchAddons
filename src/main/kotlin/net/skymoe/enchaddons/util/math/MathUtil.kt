package net.skymoe.enchaddons.util.math

import java.math.BigInteger
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

inline val Number.byte get() = toByte()
inline val Number.short get() = toShort()
inline val Number.int get() = toInt()
inline val Number.long get() = toLong()
inline val Number.float get() = toFloat()
inline val Number.double get() = toDouble()
inline val Number.floorInt get() = floor(toDouble()).toInt()
inline val Number.ceilInt get() = ceil(toDouble()).toInt()
inline val Long.bigInt: BigInteger get() = BigInteger.valueOf(this)
inline val Int.bigInt: BigInteger get() = BigInteger.valueOf(this.long)

fun lerp(
    from: Double,
    to: Double,
    progress: Double,
) = from + (to - from) * progress

fun Double.format(digits: Int = 1): String = String.format(Locale.US, "%.${digits}f", this)
