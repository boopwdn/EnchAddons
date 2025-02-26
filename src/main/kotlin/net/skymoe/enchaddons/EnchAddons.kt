package net.skymoe.enchaddons

import net.skymoe.enchaddons.api.API
import net.skymoe.enchaddons.event.EventDispatcher
import net.skymoe.enchaddons.feature.config.FeatureConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

fun getLogger(name: String): Logger = LogManager.getLogger("EnchAddons $name")

val LOGGER = getLogger("Main")

lateinit var theEA: EnchAddons

val EA by ::theEA

interface EnchAddons {
    val modID: String
    val modName: String
    val modVersion: String
    val workingDirectory: String

    val api: API
    val eventDispatcher: EventDispatcher

    val configVersion: Int

    fun <T: FeatureConfig> getConfigImpl(type: KClass<T>): T
}