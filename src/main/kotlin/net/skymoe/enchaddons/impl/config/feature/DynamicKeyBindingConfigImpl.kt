package net.skymoe.enchaddons.impl.config.feature

import cc.polyfrost.oneconfig.config.annotations.Color
import cc.polyfrost.oneconfig.config.annotations.KeyBind
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.feature.dynamickeybind.DYNAMIC_KEYBINDING_INFO
import net.skymoe.enchaddons.feature.dynamickeybind.DynamicKeyBindingConfig
import net.skymoe.enchaddons.feature.dynamickeybind.DynamicKeyBindingEvent
import net.skymoe.enchaddons.impl.config.ConfigImpl
import net.skymoe.enchaddons.impl.config.adapter.Extract
import net.skymoe.enchaddons.impl.config.announcement.DynamicSpotDependent
import net.skymoe.enchaddons.util.Colors

class DynamicKeyBindingConfigImpl :
    ConfigImpl(DYNAMIC_KEYBINDING_INFO),
    DynamicKeyBindingConfig {
    @Transient
    @Extract
    val dynamicSpotDependent = DynamicSpotDependent()

    @KeyBind(
        name = "Main action key",
    )
    var mainActionKey = OneKeyBind(UKeyboard.KEY_F1)

    @KeyBind(
        name = "Second action key",
    )
    var secondActionKey = OneKeyBind(UKeyboard.KEY_F4)

    @Text(
        name = "First line",
        size = 1,
        category = "General",
        subcategory = "Dynamic Spot",
    )
    var firstLineText: String = "Press to"

    @Color(
        name = "First Line Text Color",
        size = 1,
        category = "General",
        subcategory = "Dynamic Spot",
    )
    var firstLineTextColor = OneColor(Colors.GRAY[0].rgb)

    @Text(
        name = "Second line",
        size = 1,
        category = "General",
        subcategory = "Dynamic Spot",
    )
    var secondLineText: String = "<action>"

    @Color(
        name = "Second Line Text Color",
        size = 1,
        category = "General",
        subcategory = "Dynamic Spot",
    )
    var secondLineTextColor = OneColor(Colors.GRAY[0].rgb)

    @Color(
        name = "Key Background Color",
        size = 1,
        category = "General",
        subcategory = "Dynamic Spot",
    )
    var keyBackgroundColor = OneColor(Colors.GRAY[0].rgb)

    @Color(
        name = "Key Text Color",
        size = 1,
        category = "General",
        subcategory = "Dynamic Spot",
    )
    var keyTextColor = OneColor(Colors.GRAY[7].rgb)

    @Color(
        name = "Count Down Progress Ring Color",
        size = 2,
        category = "General",
        subcategory = "Dynamic Spot",
    )
    var countDownProgressRingColor = OneColor(Colors.GREEN[5].rgb)

    override fun postInitialized() {
        registerKeyBind(mainActionKey) {
            DynamicKeyBindingEvent.KEYPRESS.MAIN
                .also(EA.eventDispatcher)
        }
        registerKeyBind(secondActionKey) {
            DynamicKeyBindingEvent.KEYPRESS.SECOND
                .also(EA.eventDispatcher)
        }
    }
}
