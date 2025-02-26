package net.skymoe.enchaddons.feature

import net.skymoe.enchaddons.event.EventRegistry
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.registerEventEntries
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.theEA
import net.skymoe.enchaddons.util.property.versionedLazy

interface Feature<T: FeatureConfig> {
    val id: String
    val name: String
    val config: T
}

abstract class FeatureInfo<T: FeatureConfig> : Feature<T> {
    inline val configFile get() = "enchaddons-$id.json"
}

inline fun <reified T : FeatureConfig> featureInfo(
    id: String,
    name: String,
): FeatureInfo<T> {
    return object : FeatureInfo<T>() {
        override val id = id
        override val name = name
        override val config by versionedLazy(theEA::configVersion) { EA.getConfigImpl(T::class) }
    }
}

val DEFAULT_INFO = featureInfo<FeatureConfig>("unnamed-feature", "Unnamed Feature")

fun <T> T.buildRegisterEventEntries(
    function: RegistryEventDispatcher.() -> Unit,
) : List<EventRegistry.Entry> where T : EventRegistry, T : Feature<*> {
    return mutableListOf<EventRegistry.Entry>()
        .also { function(RegistryEventDispatcher(it)) }
        .also { it.registerEventEntries(theEA.eventDispatcher) }
}