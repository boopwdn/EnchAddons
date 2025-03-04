package net.skymoe.enchaddons.impl.nanovg.widget

import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.util.alphaScale
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.float
import net.skymoe.enchaddons.util.math.lerp

data class RoundedRectWidget(
    private val pos1: Vec2D,
    private val pos2: Vec2D,
    private val color: Int,
    private val radius: Double,
) : Widget<RoundedRectWidget> {
    override fun draw(context: NanoVGUIContext) {
        context.run {
            val box = pos2 - pos1
            helper.drawRoundedRect(
                vg,
                pos1.x.float,
                pos1.y.float,
                box.x.float,
                box.y.float,
                color,
                radius.float,
            )
        }
    }

    override fun alphaScale(alpha: Double) = copy(color = color alphaScale alpha)

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ) = copy(pos1 = lerp(origin, pos1, scale), pos2 = lerp(origin, pos2, scale), radius = radius * scale)
}
