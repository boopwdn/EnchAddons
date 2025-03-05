package net.skymoe.enchaddons.impl.config.announcement

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DynamicSpotDependent {
    @Info(
        text = "This feature needs DynamicSpot feature enabled.",
        type = InfoType.WARNING,
        size = 2,
    )
    var info = false
}
