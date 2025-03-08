
package net.skymoe.enchaddons.feature.config

import net.skymoe.enchaddons.api.Template
import org.apache.logging.log4j.Logger

interface NotificationOption {
    val enabled: Boolean
    val log: LogOption
    val printChat: PrintChatOption
    val title: TitleOption
    val actionBar: ActionBarOption
    val sound: SoundOption
    val sendMessage: SendMessageOption
}

inline operator fun NotificationOption.invoke(
    logger: Logger,
    placeholder: Template.() -> Unit,
) {
    if (enabled) {
        log(logger, placeholder)
        printChat(placeholder)
        title(placeholder)
        actionBar(placeholder)
        sound(placeholder)
        sendMessage(placeholder)
    }
}
