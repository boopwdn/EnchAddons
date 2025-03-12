package net.skymoe.enchaddons.feature.config

interface FeatureConfig {
    val enabled: Boolean

    fun save()
}
