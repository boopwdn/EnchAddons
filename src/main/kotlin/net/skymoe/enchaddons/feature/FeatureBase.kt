package net.skymoe.enchaddons.feature

import net.skymoe.enchaddons.event.EventRegistry
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.getLogger

abstract class FeatureBase<T : FeatureConfig>(
    featureInfo: Feature<T>,
) : Feature<T> by featureInfo,
    EventRegistry {
    val logger = getLogger(featureInfo.name)

    override val eventEntries = buildRegisterEventEntries { registerEvents(this) }

    protected abstract fun registerEvents(register: RegistryEventDispatcher)
}