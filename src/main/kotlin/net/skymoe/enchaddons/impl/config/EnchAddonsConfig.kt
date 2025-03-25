package net.skymoe.enchaddons.impl.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.SubConfig
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.getLogger
import net.skymoe.enchaddons.impl.EAImpl
import net.skymoe.enchaddons.impl.MOD_VERSION
import net.skymoe.enchaddons.impl.config.feature.DynamicKeyBindingConfigImpl
import net.skymoe.enchaddons.impl.config.feature.DynamicSpotConfigImpl
import net.skymoe.enchaddons.impl.config.feature.InvincibilityTimerConfigImpl
import net.skymoe.enchaddons.impl.config.feature.TeamSpeakConnectConfigImpl
import net.skymoe.enchaddons.impl.config.subcategory.DungeonConfig
import net.skymoe.enchaddons.util.general.inBox
import kotlin.reflect.KClass

val logger = getLogger("Config")

object EnchAddonsConfig : Config(Mod("Ench Addons $MOD_VERSION", ModType.SKYBLOCK), "ench_addons.json"), ConfigCategory {
    @Transient
    override val configImplMap = mutableMapOf<KClass<*>, () -> FeatureConfig>()

    @SubConfig
    var main = MainConfig()

    @SubConfig
    var dynamicSpot = DynamicSpotConfigImpl()

    @SubConfig
    var dynamicKeyBinding = DynamicKeyBindingConfigImpl()

    @SubConfig
    var invincibilityTimer = InvincibilityTimerConfigImpl()

    @SubConfig
    var teamSpeakConnectConfig = TeamSpeakConnectConfigImpl()

    @SubConfig
    var dungeonConfig = DungeonConfig()

    init {
        initialize()
        initializeMembers()
    }

    override fun load() {
        super.load()
        val version = ++EAImpl.configVersion
        logger.info("Increased config version to $version")
    }

    fun <T : FeatureConfig> getConfigImpl(type: KClass<T>): T =
        configImplMap[type]?.invoke()?.inBox?.cast<T>()?.also {
            logger.info("Loaded config implementation $it for type $type")
        } ?: throw NotImplementedError("$type")
}
