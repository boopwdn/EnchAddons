package net.skymoe.enchaddons.impl.config.impl

import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import net.skymoe.enchaddons.feature.config.PrintChatOption

class PrintChatOptionImpl : PrintChatOption {
    @Switch(
        name = "Enable Print Chat Message Notification",
        size = 1,
    )
    var enabledOption = false

    @Text(
        name = "Chat Message",
        size = 1,
    )
    var textOption = ""

    override val enabled by ::enabledOption
    override val text by ::textOption
}
