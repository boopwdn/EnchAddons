package net.skymoe.enchaddons.impl

import net.skymoe.enchaddons.EnchAddons
import net.skymoe.enchaddons.EnchAddonsContants.LOGGER
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.impl.api.APIImpl
import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.impl.event.EventDispatcherImpl
import net.skymoe.enchaddons.theEA
import kotlin.reflect.KClass

lateinit var theEAImpl: EnchAddonsImpl

val EAImpl by ::theEAImpl

val initEnchAddonsImpl by lazy {
    LOGGER.info("Initializing EnchAddonsImpl...")
    EnchAddonsImpl()
    LOGGER.info("Initialized EnchAddonsImpl")
}

const val MOD_ID: String = "@ID@"
const val MOD_NAME: String = "@NAME@"
const val MOD_VERSION: String = "@VER@"

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

        eventDispatcher.register<MinecraftEvent.Load.Post> {
//            loadHypixelModAPI
        }
    }
}