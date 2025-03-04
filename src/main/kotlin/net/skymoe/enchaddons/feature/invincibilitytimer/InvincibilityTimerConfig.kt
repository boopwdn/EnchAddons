package net.skymoe.enchaddons.feature.invincibilitytimer

import net.skymoe.enchaddons.feature.config.FeatureConfig

interface InvincibilityTimerConfig : FeatureConfig {
    val phoenixPetTicks: Int
    var dynamicSpotEnabled: Boolean
    var dynamicSpotExampleMode: Boolean
    var dynamicSpotLeftTextActive: String
    var dynamicSpotRightTextActive: String
    var dynamicSpotLeftTextCooldown: String
    var dynamicSpotRightTextCooldown: String
}
