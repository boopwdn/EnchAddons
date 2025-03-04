package net.skymoe.enchaddons.impl.config

import cc.polyfrost.oneconfig.hud.Hud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.Event
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.double
import net.skymoe.enchaddons.util.math.float

class AdvancedHUD(
    enabled: Boolean = false,
    x: Double = .0,
    y: Double = .0,
    positionAlignment: Int = 0,
    scale: Double = 1.0,
) : Hud(enabled, x.float, y.float, positionAlignment, scale.float) {
    @Transient
    var renderX = 0.0
        private set

    @Transient
    var renderY = 0.0
        private set

    @Transient
    var isExample = false
    private set

    val renderScale get() = scale.double
    val renderPos get() = Vec2D(renderX, renderY)

    override fun draw(matrices: UMatrixStack?, x: Float, y: Float, scale: Float, example: Boolean) {
        renderX = x.double
        renderY = y.double
        isExample = example
    }

    override fun getWidth(scale: Float, example: Boolean)
        = GetWidthEvent(this).also(EA.eventDispatcher).width.float

    override fun getHeight(scale: Float, example: Boolean)
        = GetHeightEvent(this).also(EA.eventDispatcher).height.float

    data class GetWidthEvent(
        val hud: Hud,
        var width: Double = 0.0,
    ) : Event

    data class GetHeightEvent(
        val hud: Hud,
        var height: Double = 0.0,
    ) : Event
}