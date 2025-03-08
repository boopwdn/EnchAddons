package net.skymoe.enchaddons.impl.config.impl

import cc.polyfrost.oneconfig.config.annotations.Switch
import net.skymoe.enchaddons.feature.config.NotificationOption
import net.skymoe.enchaddons.impl.config.adapter.Extract

class NotificationOptionImpl : NotificationOption {
    @Switch(
        name = "Enable Notification",
        size = 2,
    )
    var enabledOption = false

    @Extract
    var logOption = LogOptionImpl()

    @Extract
    var printChatOption = PrintChatOptionImpl()

    @Extract
    var titleOption = TitleOptionImpl()

    @Extract
    var actionBarOption = ActionBarOptionImpl()

    @Extract
    var soundOption = SoundOptionImpl()

    @Extract
    var sendMessageOption = SendMessageOptionImpl()

    override val enabled by ::enabledOption
    override val log by ::logOption
    override val printChat by ::printChatOption
    override val title by ::titleOption
    override val actionBar by ::actionBarOption
    override val sound by ::soundOption
    override val sendMessage by ::sendMessageOption
}
