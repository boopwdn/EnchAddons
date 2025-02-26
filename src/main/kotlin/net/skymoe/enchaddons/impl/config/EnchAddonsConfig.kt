package net.skymoe.enchaddons.impl.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.SubConfig
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.getLogger
import net.skymoe.enchaddons.impl.EAImpl
import net.skymoe.enchaddons.impl.MOD_VERSION
import net.skymoe.enchaddons.util.general.inBox
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

private val logger = getLogger("Config")

private val configImplMap = mutableMapOf<KClass<*>, () -> FeatureConfig>()

object EnchAddonsConfig : Config(Mod("Ench Addons $MOD_VERSION", ModType.SKYBLOCK), "ench_addons.json") {
    @SubConfig
    var main = MainConfig()

    init {
        initialize()

        EnchAddonsConfig::class
            .declaredMemberProperties
            .filter { it.returnType.isSubtypeOf(FeatureConfig::class.starProjectedType) }
            .forEach { property ->
                property(this).let { instance ->
                    logger.info("detect Config implementation $instance")
                    (instance as Config).initialize()
                    instance::class.allSuperclasses.plus(instance::class).forEach {
                        configImplMap[it] = { property(this) as FeatureConfig }
                    }
                }
            }
    }

    override fun load() {
        super.load()
        val version = ++EAImpl.configVersion
        logger.info("Increased config version to $version")
    }

    fun <T : FeatureConfig> getConfigImpl(type: KClass<T>): T {
        return configImplMap[type]?.invoke()?.inBox?.cast<T>()?.also {
            logger.info("Loaded config implementation $it for type $type")
        } ?: throw NotImplementedError("$type")
    }
}