package net.skymoe.enchaddons.util

import net.skymoe.enchaddons.util.math.int
import java.awt.Color
import kotlin.math.abs
import kotlin.math.min

fun Color.withAlpha(alpha: Int) = Color(this.red, this.green, this.blue, alpha)

fun Color.withAlpha(alpha: Float) = Color(this.red, this.green, this.blue, (alpha * 255).toInt())

fun Color.invisible() = withAlpha(0)

operator fun Color.component1() = this.red

operator fun Color.component2() = this.green

operator fun Color.component3() = this.blue

operator fun Color.component4() = this.alpha

fun convertRGBToHSL(vararg rgb: Double): DoubleArray {
    val hsl = DoubleArray(if (rgb.size >= 4) 4 else 3)
    val maxRGB = maxOf(0.0, *rgb)
    val minRGB = minOf(1.0, *rgb)
    val delta = maxRGB - minRGB
    val (r, g, b) = rgb
    hsl[2] = (maxRGB + minRGB) / 2.0
    if (delta > 0.0) {
        hsl[1] = delta / min(maxRGB + minRGB, 2 - maxRGB - minRGB)
        hsl[0] =
            when (maxRGB) {
                r -> (g - b) / delta + 6.0
                g -> (b - r) / delta + 2.0
                b -> (r - g) / delta + 4.0
                else -> 0.0
            }.let { it / 6.0 % 1.0 }
    }
    if (rgb.size >= 4) {
        hsl[3] = rgb[3]
    }
    return hsl.normalizeColor
}

private const val V06 = 0.0 / 6.0
private const val V16 = 1.0 / 6.0
private const val V26 = 2.0 / 6.0
private const val V36 = 3.0 / 6.0
private const val V46 = 4.0 / 6.0
private const val V56 = 5.0 / 6.0

fun convertHSLToRGB(vararg hsl: Double): DoubleArray {
    val (h, s, l) = hsl
    val c = (1.0 - abs(2.0 * l - 1.0)) * s
    val x = c * (1.0 - abs(h * 6.0 % 2.0 - 1.0))
    val m = l - c / 2.0

    val (rp, gp, bp) =
        when (h) {
            in V06..<V16 -> Triple(c, x, 0.0)
            in V16..<V26 -> Triple(x, c, 0.0)
            in V26..<V36 -> Triple(0.0, c, x)
            in V36..<V46 -> Triple(0.0, x, c)
            in V46..<V56 -> Triple(x, 0.0, c)
            else -> Triple(c, 0.0, x)
        }

    return if (hsl.size >= 4) {
        doubleArrayOf(rp + m, gp + m, bp + m, hsl[3])
    } else {
        doubleArrayOf(rp + m, gp + m, bp + m)
    }.normalizeColor
}

fun convertARGBToDoubleArray(argb: Int): DoubleArray =
    doubleArrayOf(
        (argb ushr 16 and 0xFF) / 255.0,
        (argb ushr 8 and 0xFF) / 255.0,
        (argb and 0xFF) / 255.0,
        (argb ushr 24 and 0xFF) / 255.0,
    ).normalizeColor

fun convertRGBToDoubleArray(argb: Int) = convertARGBToDoubleArray(argb or 0xFF000000.int)

fun convertDoubleArrayToARGB(vararg rgb: Double): Int {
    val norm = rgb.normalizeColor
    return ((norm[0] * 255.0).int shl 16) or
        ((norm[1] * 255.0).int shl 8) or
        (norm[2] * 255.0).int or
        if (rgb.size >= 4) (norm[3] * 255.0).int shl 24 else 0xFF
}

fun convertDoubleArrayToRGB(vararg rgb: Double) = convertDoubleArrayToARGB(rgb[0], rgb[1], rgb[2], 0.0)

fun convertDoubleArrayToFFRGB(vararg rgb: Double) = convertDoubleArrayToARGB(rgb[0], rgb[1], rgb[2], 1.0)

inline val DoubleArray.normalizeColor get() = DoubleArray(size) { maxOf(0.0, minOf(1.0, this[it])) }

infix fun DoubleArray.blendColor(rgbaSrc: DoubleArray): DoubleArray {
    val (srcR, srcG, srcB, srcA) = rgbaSrc
    val (dstR, dstG, dstB) = this
    val dstA = if (this.size >= 4) this[3] else 1.0
    val resA = srcA + dstA * (1.0 - srcA)
    val srcM = srcA / resA
    val dstM = dstA * (1.0 - srcA) / resA
    return doubleArrayOf(
        srcR * srcM + dstR * dstM,
        srcG * srcM + dstG * dstM,
        srcB * srcM + dstB * dstM,
        resA,
    ).normalizeColor
}

infix fun Int.blendColor(argbSrc: Int): Int =
    convertDoubleArrayToARGB(
        *convertARGBToDoubleArray(this) blendColor convertARGBToDoubleArray(argbSrc),
    )
