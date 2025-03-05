package net.skymoe.enchaddons.impl.config.feature

import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import net.skymoe.enchaddons.feature.invincibilitytimer.INVINCIBILITY_TIMER_INFO
import net.skymoe.enchaddons.feature.invincibilitytimer.InvincibilityTimerConfig
import net.skymoe.enchaddons.impl.config.ConfigImpl
import net.skymoe.enchaddons.impl.config.adapter.Extract
import net.skymoe.enchaddons.impl.config.announcement.DynamicSpotDependent

class InvincibilityTimerConfigImpl :
    ConfigImpl(INVINCIBILITY_TIMER_INFO),
    InvincibilityTimerConfig {
    @Transient
    @Extract
    val dynamicSpotDependent = DynamicSpotDependent()

    @Number(
        name = "Phoenix Pet Invincibility Ticks",
        min = 40F,
        max = 80F,
        subcategory = "General",
    )
    override var phoenixPetTicks = 40

    class DynamicSpot {
        @Switch(
            name = "Enabled",
            size = 1,
            subcategory = "Dynamic Spot",
        )
        var enabled: Boolean = true

        @Transient
        @Header(
            text = "In active",
            size = 2,
            subcategory = "Dynamic Spot",
        )
        val header = false

        @Text(
            name = "Left Text",
            size = 1,
            subcategory = "Dynamic Spot",
        )
        var leftTextActive: String = "<item.itemInfo.name>"

        @Text(
            name = "Right Text",
            size = 1,
            subcategory = "Dynamic Spot",
        )
        var rightTextActive: String = "IN: <time>"

        @Transient
        @Header(
            text = "In cooldown",
            size = 2,
            subcategory = "Dynamic Spot",
        )
        val header1 = false

        @Text(
            name = "Left Text",
            size = 1,
            subcategory = "Dynamic Spot",
        )
        var leftTextCooldown: String = "<item.itemInfo.name>"

        @Text(
            name = "Right Text",
            size = 1,
            subcategory = "Dynamic Spot",
        )
        var rightTextCooldown: String = "CD: <time>"
    }

    @Extract
    var dynamicSpot = DynamicSpot()
}
