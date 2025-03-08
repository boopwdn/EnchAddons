package net.skymoe.enchaddons.feature.invincibilitytimer

import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.feature.config.NotificationOption

interface InvincibilityTimerConfig : FeatureConfig {
    var phoenixPetTicks: Int
    val notification: Notification
}

interface Notification {
    val onItemProcced: NotificationOption
    val onCooldownEnded: NotificationOption
}
