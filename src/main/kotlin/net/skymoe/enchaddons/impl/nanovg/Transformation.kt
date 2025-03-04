package net.skymoe.enchaddons.impl.nanovg

import net.minecraft.client.gui.ScaledResolution
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.double

data class Transformation(
    val offset: Vec2D = Vec2D(0.0, 0.0),
    val scale: Double = 1.0,
) {
    infix fun translate(vec: Vec2D) = Transformation(offset + vec * scale, scale)

    infix fun scale(factor: Double) = Transformation(offset, scale * factor)

    infix fun pos(vec: Vec2D) = offset + vec * scale

    infix fun size(num: Double) = num * scale

    infix fun size(vec: Vec2D) = vec * scale

    operator fun plus(vec: Vec2D) = translate(vec)

    operator fun minus(vec: Vec2D) = translate(-vec)

    operator fun times(factor: Double) = scale(factor)

    operator fun div(factor: Double) = scale(1.0 / factor)

    operator fun not() = Transformation(-offset / scale, 1.0 / scale)

    fun scaleMC(scaleOverride: Double? = null) = scale(scaleOverride ?: ScaledResolution(MC).scaleFactor.double)

    infix fun translateScreen(vec: Vec2D) = translate(Vec2D(MC.displayWidth * vec.x, MC.displayHeight * vec.y))
}
