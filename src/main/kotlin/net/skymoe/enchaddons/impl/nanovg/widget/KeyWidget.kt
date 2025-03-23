package net.skymoe.enchaddons.impl.nanovg.widget

import cc.polyfrost.oneconfig.renderer.font.Font
import cc.polyfrost.oneconfig.renderer.font.Fonts
import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.oneconfig.loadFonts
import net.skymoe.enchaddons.impl.oneconfig.nvg
import net.skymoe.enchaddons.util.KeyCode
import net.skymoe.enchaddons.util.alphaScale
import net.skymoe.enchaddons.util.general.MutableBox
import net.skymoe.enchaddons.util.general.inMutableBox
import net.skymoe.enchaddons.util.math.*
import kotlin.math.max

data class KeyWidget(
    val key: Int,
    val pos: Vec2D,
    val height: Double,
    val progress: Double,
    val progressLineWidth: Double,
    val radiusBackground: Double,
    val size: Double,
    val padding: Double,
    val font: () -> Font = { Fonts.REGULAR },
    val colorKey: Int,
    val colorBackground: Int,
    val colorProgress: Int,
    val trBox: MutableBox<Transformation> = Transformation().inMutableBox,
    val animation: ExponentialAnimation? = null,
    val onPostRender: (Double) -> Unit,
) : Widget<KeyWidget> {
    override fun draw(context: NanoVGUIContext) {
        context.run {
            val keyName = KeyCode.getName(key)

            nvg.loadFonts(vg)
            val font = font()
            val textWidth = helper.getTextWidth(vg, keyName, size.float, font).double
            val width = max(textWidth + padding, height)

            val tr = trBox.value
            val trPos = tr pos pos

            val animatedWidth = animation?.approachOrSet(width, 0.5) ?: width
            val box = Vec2D(animatedWidth, height)
            helper.drawRoundedRect(
                vg,
                trPos.x.float,
                trPos.y.float,
                box.x.float,
                box.y.float,
                colorBackground,
                radiusBackground.float,
            )

            nvg.drawRingRectRounded(
                vg = vg,
                x = trPos.x,
                y = trPos.y,
                radius = radiusBackground,
                width = animatedWidth,
                height = height,
                progress = progress,
                lineWidth = progressLineWidth,
                color = colorProgress,
            )

            helper.drawText(
                vg,
                keyName,
                (trPos.x + (animatedWidth - textWidth) / 2).float,
                (trPos.y + size).float,
                colorKey,
                size.float,
                font,
            )

            onPostRender(animatedWidth)
        }
    }

    override fun alphaScale(alpha: Double): KeyWidget =
        copy(
            colorKey = colorKey alphaScale alpha,
            colorBackground = colorBackground alphaScale alpha,
            colorProgress = colorProgress alphaScale alpha,
        )

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ): KeyWidget =
        copy(
            pos = lerp(origin, pos, scale),
            radiusBackground = radiusBackground * scale,
        )
}
