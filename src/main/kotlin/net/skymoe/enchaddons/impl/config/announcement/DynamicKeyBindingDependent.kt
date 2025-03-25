package net.skymoe.enchaddons.impl.config.announcement

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DynamicKeyBindingDependent {
    @Info(
        text = "This feature needs DynamicKeyBinding feature enabled.",
        type = InfoType.WARNING,
        size = 2,
    )
    var info = false
}
