package net.skymoe.enchaddons.impl.nanovg.widget

import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.float
import net.skymoe.enchaddons.util.math.lerp

data class ShadowWidget(
    private val pos1: Vec2D,
    private val pos2: Vec2D,
    private val blur: Double,
    private val spread: Double,
    private val radius: Double,
    private val alpha: Double,
) : Widget<ShadowWidget> {
    override fun draw(context: NanoVGUIContext) {
        context.run {
            val box = pos2 - pos1
            helper.setAlpha(vg, alpha.float)
            helper.drawDropShadow(
                vg,
                pos1.x.float,
                pos1.y.float,
                box.x.float,
                box.y.float,
                blur.float,
                spread.float,
                radius.float,
            )
            helper.setAlpha(vg, 1.0F)
        }
    }

    override fun alphaScale(alpha: Double) = copy(alpha = this.alpha * alpha)

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ) = copy(pos1 = lerp(origin, pos1, scale), pos2 = lerp(origin, pos2, scale), radius = radius * scale)
}
