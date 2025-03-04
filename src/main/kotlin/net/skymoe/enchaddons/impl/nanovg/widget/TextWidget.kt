package net.skymoe.enchaddons.impl.nanovg.widget

import cc.polyfrost.oneconfig.renderer.font.Font
import cc.polyfrost.oneconfig.renderer.font.Fonts
import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.oneconfig.loadFonts
import net.skymoe.enchaddons.impl.oneconfig.nvg
import net.skymoe.enchaddons.util.alphaScale
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.double
import net.skymoe.enchaddons.util.math.float
import net.skymoe.enchaddons.util.math.lerp

data class TextWidget(
    private val text: String,
    private val pos: Vec2D,
    private val color: Int,
    private val size: Double,
    private val font: () -> Font = { Fonts.REGULAR },
    private val anchor: Vec2D = Vec2D(0.0, 0.0),
    private val widthLimit: Double? = null,
    private val ellipsis: String = "",
) : Widget<TextWidget> {
    override fun draw(context: NanoVGUIContext) {
        context.run {
            nvg.loadFonts(vg)
            val font = font()
            var width = helper.getTextWidth(vg, text, size.float, font).double
            var textToRender = text
            if (widthLimit !== null && width >= widthLimit) {
                width = helper.getTextWidth(vg, ellipsis, size.float, font).double
                textToRender =
                    StringBuilder()
                        .apply {
                            text.firstOrNull {
                                val newWidth = width + helper.getTextWidth(vg, it.toString(), size.float, font).double
                                if (newWidth < widthLimit) {
                                    width = newWidth
                                    append(it)
                                    false
                                } else {
                                    true
                                }
                            }
                        }.append(ellipsis)
                        .toString()
            }
            val x = pos.x - width * anchor.x
            val y = pos.y - size * anchor.y + size / 2.0
            helper.drawText(vg, textToRender, x.float, y.float, color, size.float, font)
        }
    }

    override fun alphaScale(alpha: Double) = copy(color = color alphaScale alpha)

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ) = copy(pos = lerp(origin, pos, scale), size = size * scale, widthLimit = widthLimit?.times(scale))
}
