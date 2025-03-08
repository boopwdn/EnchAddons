package net.skymoe.enchaddons.feature.invincibilitytimer

import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.feature.config.NotificationOption

interface InvincibilityTimerConfig : FeatureConfig {
    var phoenixPetTicks: Int
    val notification: Notification
}

interface Notification {
    var onItemProcced: NotificationOption
    var onCooldownEnded: NotificationOption
}
