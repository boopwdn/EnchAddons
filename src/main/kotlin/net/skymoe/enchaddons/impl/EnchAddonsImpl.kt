package net.skymoe.enchaddons.impl

import net.skymoe.enchaddons.EnchAddons
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpot
import net.skymoe.enchaddons.feature.invincibilitytimer.InvincibilityTimer
import net.skymoe.enchaddons.getLogger
import net.skymoe.enchaddons.impl.api.APIImpl
import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.impl.event.EventDispatcherImpl
import net.skymoe.enchaddons.impl.feature.DynamicSpotHUD
import net.skymoe.enchaddons.impl.hypixel.loadHypixelModAPI
import net.skymoe.enchaddons.theEA
import kotlin.reflect.KClass

lateinit var theEAImpl: EnchAddonsImpl

val EAImpl by ::theEAImpl

val LOGGER = getLogger("Main")

val initEnchAddonsImpl by lazy {
    LOGGER.info("Initializing EnchAddonsImpl...")
    EnchAddonsImpl()
    LOGGER.info("Initialized EnchAddonsImpl")
}

const val MOD_ID: String = "enchaddons"
const val MOD_NAME: String = "Ench Addons"
const val MOD_VERSION: String = "0.1.0"

class EnchAddonsImpl : EnchAddons {
    init {
        theEA = this
        theEAImpl = this
    }

    override val modID = MOD_ID
    override val modName: String = MOD_NAME
    override val modVersion: String = MOD_VERSION
    override val workingDirectory: String = "."

    override val api = APIImpl()
    override val eventDispatcher = EventDispatcherImpl()

    override var configVersion = 0

    override fun <T : FeatureConfig> getConfigImpl(type: KClass<T>) = EnchAddonsConfig.getConfigImpl(type)

    init {
        EnchAddonsConfig

        DynamicSpot
        DynamicSpotHUD

        InvincibilityTimer

        eventDispatcher.register<MinecraftEvent.Load.Post> {
            loadHypixelModAPI
        }
    }
}
