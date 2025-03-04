package net.skymoe.enchaddons.impl.hypixel

import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.EventPacket
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.HypixelLocation
import net.skymoe.enchaddons.api.HypixelServerType
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.hypixel.HypixelAPIEvent
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.DEFAULT_INFO
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.impl.EAImpl
import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.printChat
import net.skymoe.enchaddons.util.property.versionedLazy
import kotlin.jvm.optionals.getOrNull

class HypixelModAPIWrapper(
    private val api: HypixelModAPI,
) : FeatureBase<FeatureConfig>(DEFAULT_INFO) {
    private inline fun <reified T : EventPacket> registerPacket(noinline handler: (T) -> Unit) {
        api.subscribeToEventPacket(T::class.java)
        api.createHandler(T::class.java, handler)
    }

    init {
        registerPacket<ClientboundLocationPacket> { packet ->
            val location =
                HypixelLocation(
                    packet.serverName,
                    packet.serverType.getOrNull()?.let { HypixelServerType(it.name) },
                    packet.lobbyName.getOrNull(),
                    packet.mode.getOrNull(),
                    packet.map.getOrNull(),
                )
            if (EnchAddonsConfig.main.verboseHypixelModAPI) {
                printChat("EAImpl.api.hypixelLocation = $location")
            }
            EAImpl.api.hypixelLocation = location
            EA.eventDispatcher(HypixelAPIEvent.Location(location))
        }
    }

    private val resetLocation by versionedLazy({ MC.isIntegratedServerRunning to MC.currentServerData }) {
        if (EnchAddonsConfig.main.verboseHypixelModAPI) {
            printChat("EAImpl.api.hypixelLocation = null")
        }
        EAImpl.api.hypixelLocation = null
        EA.eventDispatcher(HypixelAPIEvent.Location(null))
    }

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
            register<MinecraftEvent.Tick.Pre> {
                resetLocation
            }
        }
    }
}