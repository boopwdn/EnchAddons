package net.skymoe.enchaddons.impl.nanovg

import net.skymoe.enchaddons.util.math.Vec2D

interface Widget<T: Widget<T>> {
    fun draw(context: NanoVGUIContext)

    fun alphaScale(alpha: Double): T

    fun scale(
        scale: Double,
        origin: Vec2D,
    ): T
}