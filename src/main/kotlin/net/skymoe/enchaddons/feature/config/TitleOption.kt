package net.skymoe.enchaddons.feature.config

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.Template
import net.skymoe.enchaddons.api.format
import net.skymoe.enchaddons.util.MC

interface TitleOption {
    val enabled: Boolean
    val setTime: Boolean
    val fadeIn: Int
    val stay: Int
    val fadeOut: Int
    val text: String
    val setSubtitle: Boolean
    val subtitle: String
}

inline operator fun TitleOption.invoke(placeholder: Template.() -> Unit) {
    if (enabled && MC.theWorld !== null) {
        if (setTime) {
            MC.ingameGUI.displayTitle(null, null, fadeIn, stay, fadeOut)
        }
        MC.ingameGUI.displayTitle(
            EA.api.format(text, placeholder),
            null,
            0,
            0,
            0,
        )
        if (setSubtitle) {
            MC.ingameGUI.displayTitle(
                null,
                EA.api.format(subtitle, placeholder),
                0,
                0,
                0,
            )
        }
    }
}
