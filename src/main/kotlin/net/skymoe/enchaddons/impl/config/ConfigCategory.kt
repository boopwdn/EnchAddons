package net.skymoe.enchaddons.impl.config

import cc.polyfrost.oneconfig.config.Config
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.util.general.inBox
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

interface ConfigCategory {
    val configImplMap: MutableMap<KClass<*>, () -> FeatureConfig>

    fun initializeMembers() {
        this::class
            .declaredMemberProperties
            .filter { it.returnType.isSubtypeOf(FeatureConfig::class.starProjectedType) }
            .forEach { property ->
                val propertyCasted = property.inBox.cast<KProperty1<ConfigCategory, *>>()
                propertyCasted(this).let { instance ->
                    logger.info("Detect Config implementation $instance")
                    (instance as Config).initialize()
                    (instance as ConfigImpl).postInitialized()
                    instance::class.allSuperclasses.plus(instance::class).forEach {
                        configImplMap[it] = { propertyCasted(this) as FeatureConfig }
                    }
                    if (instance is ConfigCategory) {
                        configImplMap += instance.configImplMap
                    }
                }
            }
    }
}
