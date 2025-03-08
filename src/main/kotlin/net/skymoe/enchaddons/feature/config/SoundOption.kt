package net.skymoe.enchaddons.feature.config

import net.minecraft.util.ResourceLocation
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.Template
import net.skymoe.enchaddons.api.format
import net.skymoe.enchaddons.util.CustomSound
import net.skymoe.enchaddons.util.MC

interface SoundOption {
    val enabled: Boolean
    val name: String
    val volume: Double
    val pitch: Double
}

inline operator fun SoundOption.invoke(placeholder: Template.() -> Unit) {
    if (enabled && MC.theWorld != null) {
        MC.soundHandler.playSound(
            CustomSound(
                ResourceLocation(EA.api.format(name, placeholder)),
                volume,
                pitch,
            ),
        )
    }
}
