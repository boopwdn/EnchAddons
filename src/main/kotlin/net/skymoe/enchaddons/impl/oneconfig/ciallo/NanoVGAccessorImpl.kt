package net.skymoe.enchaddons.impl.oneconfig.ciallo

import cc.polyfrost.oneconfig.renderer.font.Font
import net.skymoe.enchaddons.impl.MOD_ID
import net.skymoe.enchaddons.impl.oneconfig.NanoVGAccessor
import net.skymoe.enchaddons.impl.oneconfig.NanoVGImageCacheEntry
import net.skymoe.enchaddons.impl.oneconfig.nvg
import net.skymoe.enchaddons.util.convertARGBToDoubleArray
import net.skymoe.enchaddons.util.math.float
import net.skymoe.enchaddons.util.scope.withscope
import net.skymoe.enchaddons.util.toBuffer
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.NVG_IMAGE_NODELETE
import org.lwjgl.nanovg.NanoVGGL3.nvglCreateImageFromHandle
import java.nio.ByteBuffer
import java.util.UUID
import kotlin.math.*

private fun NVGColor.fill(argb: Int): NVGColor {
    val (rv, gv, bv, av) = convertARGBToDoubleArray(argb)
    r(rv.float)
    g(gv.float)
    b(bv.float)
    a(av.float)
    return this
}

object NanoVGAccessorImpl : NanoVGAccessor {
    init {
        nvg = this
    }

    override fun loadFont(
        vg: Long,
        name: String,
    ): Font {
        val byteArray = javaClass.getResource("/assets/$MOD_ID/font/$name")!!.readBytes()
        val byteBuffer =
            ByteBuffer.allocateDirect(byteArray.size).apply {
                put(byteArray)
                flip()
            }
        val fontFace = UUID.randomUUID().toString()
        nvgCreateFontMem(vg, fontFace, byteBuffer, 0)
        return object : Font(fontFace, "") {
            val byteArray = byteBuffer
        }
    }

    override fun deleteImages(
        vg: Long,
        images: Set<Int>,
    ) {
        images.forEach {
            nvgDeleteImage(vg, it)
        }
    }

    override fun drawRingRectRounded(
        vg: Long,
        x: Double,
        y: Double,
        radius: Double,
        width: Double,
        height: Double,
        progress: Double,
        lineWidth: Double,
        color: Int,
    ) {
        if (progress < 0.0) return

        withscope {
            val nvgColor = NVGColor.calloc().using().fill(color)

            val totalLength = 2 * (width + height) - (8 - 2 * PI) * radius
            val widthWithoutR = (width - 2 * radius) / totalLength
            val heightWithoutR = (height - 2 * radius) / totalLength
            val r = (PI * radius) / (2 * totalLength)

            val progressList =
                listOf(
                    0.0,
                    widthWithoutR * 0.5,
                    widthWithoutR * 0.5 + r,
                    widthWithoutR * 0.5 + heightWithoutR + r,
                    widthWithoutR * 0.5 + heightWithoutR + r * 2.0,
                    widthWithoutR * 1.5 + heightWithoutR + r * 2.0,
                    widthWithoutR * 1.5 + heightWithoutR + r * 3.0,
                    widthWithoutR * 1.5 + heightWithoutR * 2.0 + r * 3.0,
                    widthWithoutR * 1.5 + heightWithoutR * 2.0 + r * 4.0,
                    widthWithoutR * 2.0 + heightWithoutR * 2.0 + r * 4.0,
                    Double.POSITIVE_INFINITY,
                )

            val (segmentIndex, segmentStart) = progressList.withIndex().first { (i, v) -> v <= progress && progress < progressList[i + 1] }

            val progressInSegment = (progress - segmentStart) / (progressList[segmentIndex + 1] - segmentStart)

            nvgBeginPath(vg)
            nvgStrokeColor(vg, nvgColor)
            nvgStrokeWidth(vg, lineWidth.toFloat())
            nvgLineCap(vg, NVG_ROUND)
            nvgLineJoin(vg, NVG_ROUND)

            // Calculate the coordinates for the starting point (top-middle)
            val startX = x + width / 2.0
            val startY = y

            nvgMoveTo(vg, startX.toFloat(), startY.toFloat())

            // Helper function to draw segments
            fun drawSegment(
                segment: Int,
                segmentProgress: Double,
            ) {
                when (segment) {
                    0 -> { // Top segment
                        val segmentLength = width - 2 * radius
                        val currentX = x + width / 2.0 + (segmentLength / 2) * segmentProgress
                        nvgLineTo(vg, currentX.toFloat(), y.toFloat())
                    }
                    1 -> { // Top-Right Corner
                        val angle = (segmentProgress - 1.0) * PI / 2
                        nvgArc(
                            vg,
                            (x + width - radius).toFloat(),
                            (y + radius).toFloat(),
                            radius.toFloat(),
                            (-PI / 2).toFloat(),
                            angle.float,
                            NVG_CW,
                        )
                    }
                    2 -> { // Right segment
                        val segmentLength = height - 2 * radius
                        val currentY = y + radius + segmentLength * segmentProgress
                        nvgLineTo(vg, (x + width).toFloat(), currentY.toFloat())
                    }
                    3 -> { // Bottom-Right Corner
                        val angle = segmentProgress * PI / 2
                        nvgArc(
                            vg,
                            (x + width - radius).toFloat(),
                            (y + height - radius).toFloat(),
                            radius.toFloat(),
                            0f,
                            angle.float,
                            NVG_CW,
                        )
                    }
                    4 -> { // Bottom segment
                        val segmentLength = width - 2 * radius
                        val currentX = x + width - radius - segmentLength * segmentProgress
                        nvgLineTo(vg, currentX.toFloat(), (y + height).toFloat())
                    }
                    5 -> { // Bottom-Left Corner
                        val angle = (segmentProgress + 1.0) * PI / 2
                        nvgArc(
                            vg,
                            (x + radius).toFloat(),
                            (y + height - radius).toFloat(),
                            radius.toFloat(),
                            (PI / 2).toFloat(),
                            angle.float,
                            NVG_CW,
                        )
                    }
                    6 -> { // Left segment
                        val segmentLength = height - 2 * radius
                        val currentY = y + height - radius - segmentLength * segmentProgress
                        nvgLineTo(vg, x.toFloat(), currentY.toFloat())
                    }
                    7 -> { // Top-Left Corner
                        val angle = (segmentProgress + 2.0) * PI / 2
                        nvgArc(
                            vg,
                            (x + radius).toFloat(),
                            (y + radius).toFloat(),
                            radius.toFloat(),
                            PI.toFloat(),
                            angle.float,
                            NVG_CW,
                        )
                    }
                    8 -> { // Top segment (partial, completing the circle)
                        val segmentLength = width / 2.0 - radius // Half of the width to the center
                        val currentX = x + radius + segmentLength * segmentProgress
                        nvgLineTo(vg, currentX.toFloat(), y.toFloat())
                    }
                }
            }

            // Draw all the complete segments up to the current one
            for (i in 0 until segmentIndex) {
                drawSegment(i, 1.0)
            }

            // Draw the last segment, the incomplete one
            drawSegment(segmentIndex, progressInSegment)

            nvgStroke(vg)
            nvgClosePath(vg)
        }
    }

    override fun drawRingArc(
        vg: Long,
        x: Double,
        y: Double,
        outerRadius: Double,
        innerRadius: Double,
        fromRadian: Double,
        toRadian: Double,
        arcPaddingFrom: Double,
        arcPaddingTo: Double,
        color: Int,
    ) {
        withscope {
            val nvgColor = NVGColor.calloc().using().fill(color)

            val lpo = asin(arcPaddingFrom / outerRadius)
            val lpi = asin(arcPaddingFrom / innerRadius)
            val rpo = asin(arcPaddingTo / outerRadius)
            val rpi = asin(arcPaddingTo / innerRadius)

            nvgBeginPath(vg)
            nvgArc(
                vg,
                x.float,
                y.float,
                outerRadius.float,
                (fromRadian + lpo).float,
                (toRadian - rpo).float,
                NVG_CW,
            )
            nvgArc(
                vg,
                x.float,
                y.float,
                innerRadius.float,
                (toRadian - rpi).float,
                (fromRadian + lpi).float,
                NVG_CCW,
            )
            nvgClosePath(vg)
            nvgFillColor(vg, nvgColor)
            nvgFill(vg)
        }
    }

    override fun drawRoundedImage(
        vg: Long,
        image: Int,
        imageXRel: Double,
        imageYRel: Double,
        imageWRel: Double,
        imageHRel: Double,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        alpha: Double,
        radius: Double,
    ) {
        withscope {
            val nvgPaint = NVGPaint.calloc().using()
            val imageW = width / imageWRel
            val imageH = height / imageHRel
            val imageX = x - imageW * imageXRel
            val imageY = y - imageH * imageYRel
            nvgImagePattern(
                vg,
                imageX.float,
                imageY.float,
                imageW.float,
                imageH.float,
                0.0F,
                image,
                alpha.float,
                nvgPaint,
            )
            nvgBeginPath(vg)
            nvgRoundedRect(
                vg,
                x.float,
                y.float,
                width.float,
                height.float,
                radius.float,
            )
            nvgFillPaint(vg, nvgPaint)
            nvgFill(vg)
        }
    }

    override fun loadImageFromByteArray(
        vg: Long,
        image: ByteArray,
    ): Int = nvgCreateImageMem(vg, NVG_IMAGE_NEAREST, image.toBuffer())

    override fun drawRoundedPlayerAvatar(
        vg: Long,
        imageCache: NanoVGImageCacheEntry,
        texture: Int,
        hat: Boolean,
        scaleHat: Boolean,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        alpha: Double,
        radius: Double,
    ) {
        imageCache.cleanup(this, vg)

        val nvgImage =
            imageCache.cache.getOrPut(texture) {
                nvglCreateImageFromHandle(vg, texture, 64, 64, NVG_IMAGE_NEAREST or NVG_IMAGE_NODELETE)
                    .also { if (it == -1) return@drawRoundedPlayerAvatar }
            }

        drawRoundedImage(
            vg,
            nvgImage,
            0.125,
            0.125,
            0.125,
            0.125,
            x,
            y,
            width,
            height,
            alpha,
            radius,
        )

        if (hat) {
            val offset = if (scaleHat) 1.0 / 144.0 else 0.0

            drawRoundedImage(
                vg,
                nvgImage,
                0.625 + offset,
                0.125 + offset,
                0.125 - 2.0 * offset,
                0.125 - 2.0 * offset,
                x,
                y,
                width,
                height,
                alpha,
                radius,
            )
        }
    }
}
