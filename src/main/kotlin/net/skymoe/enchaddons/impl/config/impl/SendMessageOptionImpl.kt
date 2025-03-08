package net.skymoe.enchaddons.impl.config.impl

import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.annotations.Number
import net.skymoe.enchaddons.feature.config.SendMessageOption
import net.skymoe.enchaddons.feature.config.SendMessagePool
import net.skymoe.enchaddons.impl.config.adapter.Extract
import net.skymoe.enchaddons.util.printChat

class SendMessageOptionImpl : SendMessageOption {
    @Switch(
        name = "Enable Send Message Notification",
        size = 1,
    )
    var enabledOption = false

    @Text(
        name = "Message",
        size = 1,
    )
    var textOption = ""

    @Checkbox(
        name = "Enable Interval",
        size = 1,
    )
    var enableIntervalOption = false

    @Number(
        name = "Interval Since Last Message (in ticks)",
        min = 0.0F,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var intervalOption = 0

    @Text(
        name = "Interval Pool",
        size = 1,
    )
    var intervalPoolOption = "default"

    @Number(
        name = "Max Pool Size",
        min = 0.0F,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var maxPoolSizeOption = 2147483647

    override val enabled by ::enabledOption
    override val text by ::textOption
    override val enableInterval by ::enableIntervalOption
    override val interval by ::intervalOption
    override val intervalPool by ::intervalPoolOption
    override val maxPoolSize by ::maxPoolSizeOption

    @Transient
    @Extract
    val clearPool =
        @Button(
            name = "Clear this Pool",
            text = "Clear",
            size = 1,
        )
        {
            SendMessagePool.clear(intervalPool)
            printChat("cleared pool $intervalPool")
        }

    @Transient
    @Extract
    val viewPoolSize =
        @Button(
            name = "Print Pool Sizes",
            text = "Print",
            size = 1,
        )
        {
            printChat()
            printChat("Pool Sizes:")
            SendMessagePool.poolMap.forEach { (pool, list) ->
                if (list.isNotEmpty()) {
                    printChat("    $pool: ${list.size}")
                }
            }
            printChat()
        }
}
