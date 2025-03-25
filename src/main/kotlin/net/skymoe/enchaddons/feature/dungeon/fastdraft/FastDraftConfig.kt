package net.skymoe.enchaddons.feature.dungeon.fastdraft

import net.skymoe.enchaddons.feature.config.FeatureConfig

interface FastDraftConfig : FeatureConfig {
    val chatHintEnabled: Boolean
    val dynamicKeyBindingEnabled: Boolean
    val actionText: String
}
