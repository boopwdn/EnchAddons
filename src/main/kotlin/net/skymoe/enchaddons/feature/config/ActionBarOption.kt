package net.skymoe.enchaddons.feature.config

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.Template
import net.skymoe.enchaddons.api.format
import net.skymoe.enchaddons.util.MC

interface ActionBarOption {
    val enabled: Boolean
    val chroma: Boolean
    val text: String
}

inline operator fun ActionBarOption.invoke(placeholder: Template.() -> Unit) {
    if (enabled && MC.theWorld !== null) {
        MC.ingameGUI.setRecordPlaying(
            EA.api.format(text, placeholder),
            chroma,
        )
    }
}
