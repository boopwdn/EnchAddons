
package net.skymoe.enchaddons.impl.config.impl

import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import net.skymoe.enchaddons.feature.config.SoundOption
import net.skymoe.enchaddons.util.math.double

class SoundOptionImpl : SoundOption {
    @Switch(
        name = "Enable Sound Notification",
        size = 1,
    )
    var enabledOption = false

    @Text(
        name = "Sound",
        size = 1,
    )
    var nameOption = ""

    @Number(
        name = "Volume",
        min = 0.0F,
        max = 1.0F,
        size = 1,
    )
    var volumeOption = 1.0F

    @Number(
        name = "Pitch",
        min = 0.0F,
        max = 2.0F,
        size = 1,
    )
    var pitchOption = 1.0F

    override val enabled by ::enabledOption
    override val name by ::nameOption
    override val volume get() = volumeOption.double
    override val pitch get() = pitchOption.double
}
