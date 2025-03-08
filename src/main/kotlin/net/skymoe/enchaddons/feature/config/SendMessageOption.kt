package net.skymoe.enchaddons.feature.config

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.Template
import net.skymoe.enchaddons.api.format
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.general.MutableBox
import net.skymoe.enchaddons.util.general.inMutableBox

interface SendMessageOption {
    val enabled: Boolean
    val enableInterval: Boolean
    val intervalPool: String
    val interval: Int
    val maxPoolSize: Int
    val text: String
}

object SendMessagePool :
    FeatureBase<FeatureConfig>(featureInfo("send_message_pool", "Send Message Pool")) {
    val poolMap = mutableMapOf<String, ArrayDeque<Pair<String, MutableBox<Int>>>>()

    fun add(
        pool: String,
        interval: Int,
        message: String,
        max: Int,
    ) {
        synchronized(this) {
            if (poolMap.size < max) {
                poolMap.getOrPut(pool) { ArrayDeque() }.addLast(message to (-interval).inMutableBox)
            }
        }
    }

    fun clear(pool: String) {
        synchronized(this) {
            poolMap[pool]?.clear()
        }
    }

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
            register<MinecraftEvent.World.Unload> {
                synchronized(this) {
                    poolMap.clear()
                }
            }

            register<MinecraftEvent.Tick.Pre> {
                synchronized(this) {
                    poolMap.forEach { (_, list) ->
                        while (true) {
                            list.firstOrNull()?.let { (message, interval) ->
                                if (interval.value == 0) {
                                    MC.thePlayer.sendChatMessage(message)
                                    return@let
                                } else if (interval.value < 0) {
                                    interval.value = -interval.value
                                    MC.thePlayer.sendChatMessage(message)
                                }
                                --interval.value
                                if (interval.value == 0) {
                                    list.removeFirstOrNull()
                                }
                                return@forEach
                            } ?: break
                        }
                    }
                }
            }
        }
    }
}

inline operator fun SendMessageOption.invoke(placeholder: Template.() -> Unit) {
    if (enabled && MC.theWorld != null) {
        val messages = EA.api.format(text, placeholder).split("\n")
        if (enableInterval) {
            messages.forEach { SendMessagePool.add(intervalPool, interval, it, maxPoolSize) }
        } else {
            messages.forEach(MC.thePlayer::sendChatMessage)
        }
    }
}
