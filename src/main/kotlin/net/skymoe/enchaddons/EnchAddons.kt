package net.skymoe.enchaddons

import net.skymoe.enchaddons.api.API
import net.skymoe.enchaddons.event.EventDispatcher
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.impl.MOD_ID
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.net.URI
import java.net.URL
import kotlin.reflect.KClass

fun getLogger(name: String): Logger = LogManager.getLogger("EnchAddons $name")

val LOGGER = getLogger("Main")

lateinit var theEA: EnchAddons

val EA by ::theEA

val CLASS_ROOT: URL by lazy {
    val uri = EnchAddons::class.java.getResource("/mixins.$MOD_ID.json")!!.toURI()
    if (uri.scheme == "jar") {
        URI(uri.toASCIIString().replace(Regex("^jar:(file:.*[.]jar)!/.*")) { it.groupValues[1] }).toURL()
    } else {
        uri.let {
            if (it.path.endsWith("/java/main/mixins.$MOD_ID.json")) {
                File(File(it).parentFile.parentFile.parentFile, "/kotlin/main").toURI()
            } else {
                File(it).parentFile.toURI()
            }.toURL()
        }
    }
}

interface EnchAddons {
    val modID: String
    val modName: String
    val modVersion: String
    val workingDirectory: String

    val api: API
    val eventDispatcher: EventDispatcher

    val configVersion: Int

    fun <T : FeatureConfig> getConfigImpl(type: KClass<T>): T
}
