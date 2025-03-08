package net.skymoe.enchaddons.impl.config.impl

import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import net.skymoe.enchaddons.util.math.double

class ScreenScaleOptionImpl {
    @Switch(
        name = "Override Screen Scale",
        size = 1,
    )
    var overrideEnabledOption: Boolean = false

    @Number(
        name = "Override Screen Scale Value",
        min = 0.125F,
        max = Float.MAX_VALUE,
        size = 1,
    )
    var scaleOption: Float = 1.0F

    val overrideEnabled by ::overrideEnabledOption
    val scale get() = scaleOption.double

    val nullableScale get() = scale.takeIf { overrideEnabled }
}
