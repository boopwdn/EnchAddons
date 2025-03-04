package net.skymoe.enchaddons.impl.config.feature

import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import net.skymoe.enchaddons.feature.invincibilitytimer.INVINCIBILITY_TIMER_INFO
import net.skymoe.enchaddons.feature.invincibilitytimer.InvincibilityTimerConfig
import net.skymoe.enchaddons.impl.config.ConfigImpl

class InvincibilityTimerConfigImpl :
    ConfigImpl(INVINCIBILITY_TIMER_INFO),
    InvincibilityTimerConfig {
    @Number(
        name = "Phoenix Pet Invincibility Ticks",
        min = 40F,
        max = 80F,
    )
    override val phoenixPetTicks = 40

    @Switch(
        name = "Enabled",
        size = 1,
        subcategory = "Dynamic Spot",
    )
    override var dynamicSpotEnabled = false

    @Switch(
        name = "Example Mode",
        size = 1,
        subcategory = "DynamicSpot",
    )
    override var dynamicSpotExampleMode = false

    @Header(
        text = "In active",
        size = 2,
    )
    @Text(
        name = "Left Text",
        size = 1,
    )
    override var dynamicSpotLeftTextActive = "<name>"

    @Text(
        name = "Right Text",
        size = 1,
    )
    override var dynamicSpotRightTextActive = "无敌: <time>"

    @Header(
        text = "In cooldown",
        size = 2,
    )
    @Text(
        name = "Left Text",
        size = 1,
    )
    override var dynamicSpotLeftTextCooldown = "<name>"

    @Text(
        name = "Right Text",
        size = 1,
    )
    override var dynamicSpotRightTextCooldown = "冷却: <time>"
}
