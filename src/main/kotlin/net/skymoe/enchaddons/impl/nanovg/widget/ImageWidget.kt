package net.skymoe.enchaddons.impl.nanovg.widget

import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.oneconfig.nvg
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.lerp

private val nvgImageCache: HashMap<ByteArray, Int> = hashMapOf()

data class ImageWidget(
    private val pos: Vec2D,
    private val size: Vec2D,
    private val image: (Long) -> Int,
    private val alpha: Double,
    private val radius: Double,
) : Widget<ImageWidget> {
    override fun draw(context: NanoVGUIContext) {
        context.run {
            nvg.drawRoundedImage(
                vg,
                image(vg),
                0.0,
                0.0,
                1.0,
                1.0,
                pos.x,
                pos.y,
                size.x,
                size.y,
                alpha,
                radius,
            )
        }
    }

    override fun alphaScale(alpha: Double) = copy(alpha = this.alpha * alpha)

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ) = copy(pos = lerp(origin, pos, scale), size = size * scale, radius = radius * scale)
}
