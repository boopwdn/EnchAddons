package net.skymoe.enchaddons.impl.nanovg.widget

import net.skymoe.enchaddons.util.math.Vec2D

fun backgroundWidget(
    pos: Vec2D,
    size: Vec2D,
    padding: Vec2D,
    color: Int,
    radius: Double,
    blur: Double,
): ListWidget {
    return ListWidget(
        ShadowWidget(
            pos - padding,
            pos + size + padding,
            blur,
            0.0,
            radius,
            1.0,
        ),
        RoundedRectWidget(
            pos - padding,
            pos + size + padding,
            color,
            radius,
        ),
    )
}
