package net.skymoe.enchaddons.feature.config

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.Template
import net.skymoe.enchaddons.api.format
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.printChat

interface PrintChatOption {
    val enabled: Boolean
    val text: String
}

inline operator fun PrintChatOption.invoke(placeholder: Template.() -> Unit) {
    if (enabled && MC.theWorld != null) {
        printChat(EA.api.format(text, placeholder))
    }
}
