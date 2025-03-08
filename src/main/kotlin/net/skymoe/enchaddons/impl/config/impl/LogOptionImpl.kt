package net.skymoe.enchaddons.impl.config.impl

import cc.polyfrost.oneconfig.config.annotations.Dropdown
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import net.skymoe.enchaddons.feature.config.LogOption

class LogOptionImpl : LogOption {
    @Switch(
        name = "Enable Log Notification",
        size = 2,
    )
    var enabledOption = false

    @Text(
        name = "Log Text",
        size = 1,
    )
    var textOption = ""

    @Dropdown(
        name = "Log Level",
        options = ["FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE"],
        size = 1,
    )
    var levelOption = 3

    override val enabled by ::enabledOption
    override val text by ::textOption
    override val level by ::levelOption
}
