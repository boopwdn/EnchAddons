package net.skymoe.enchaddons.feature.dungeon.fastdraft

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.minecraft.ChatEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.dynamickeybind.DynamicKeyBindingEvent
import net.skymoe.enchaddons.feature.ensureEnabled
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.util.*
import net.skymoe.enchaddons.util.scope.longrun

val FAST_DRAFT_INFO = featureInfo<FastDraftConfig>("fast_draft", "Fast Draft")

val puzzleFailPatterns =
    listOf(
        Regex("(?:§r§c§lPUZZLE FAIL!|§4) §.§.(?<name>\\S*) .*"),
        Regex("§r§4\\[STATUE] Oruo the Omniscient§r§f: (?:§.)*(?<name>\\S*) (?:§.)*chose the wrong .*"),
    )

object FastDraft : FeatureBase<FastDraftConfig>(FAST_DRAFT_INFO) {
    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
            register<ChatEvent.Normal.Pre> { event ->
                longrun {
                    ensureEnabled()

                    println(event.messageRaw)

                    puzzleFailPatterns.forEach { pattern ->
                        pattern.matchEntire(event.messageRaw)?.let { matchResult ->
                            if (config.chatHintEnabled) {
                                val name = matchResult.groups["name"]?.value
                                val message =
                                    (
                                        "§c§lPUZZLE FAILED! §r§b$name §r§efailed a puzzle. \n" +
                                            "§eClick here to get §5Architect's First Draft"
                                    ).asComponent().also {
                                        it.command = "/gfs architect_first_draft"
                                        it.hover = "§eClick to get from sacks!".asComponent()
                                    }
                                printChat(message)
                            }

                            if (config.dynamicKeyBindingEnabled) {
                                DynamicKeyBindingEvent
                                    .Add(
                                        element =
                                            object : DynamicKeyBindingEvent.DynamicKeyBindingElement(
                                                aliveTime = 100,
                                                id = "fast_draft",
                                                action = {
                                                    MC.thePlayer.sendChatMessage("/gfs architect_first_draft")
                                                },
                                                actionText = config.actionText,
                                            ) {},
                                    ).also(EA.eventDispatcher)
                            }
                            return@forEach
                        }
                    }
                }
            }
        }
    }
}
