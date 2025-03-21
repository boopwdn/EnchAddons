package net.skymoe.enchaddons.feature.teamspeakconnect

import net.skymoe.enchaddons.feature.config.FeatureConfig

interface TeamSpeakConnectConfig : FeatureConfig {
    val address: String
    var apiKey: String
    val dynamicSpot: DynamicSpot
}

interface DynamicSpot {
    val displayTime: Int
}
