package net.skymoe.enchaddons.impl.mixincallback

import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S32PacketConfirmTransaction
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.hypixel.SkyblockEvent
import net.skymoe.enchaddons.event.minecraft.ChatEvent
import net.skymoe.enchaddons.util.math.int
import net.skymoe.enchaddons.util.trimStyle

object NetHandlerPlayClientCallback {
    fun onS02PacketChatPost(packet: S02PacketChat) {
        when (packet.type.int) {
            2 -> {}
            else -> {
                ChatEvent.Normal
                    .Post(packet.chatComponent.formattedText.trimStyle)
                    .also(EA.eventDispatcher)
            }
        }
    }

    fun onS32PacketConfirmTransactionPre(packet: S32PacketConfirmTransaction) {
        SkyblockEvent.ServerTick
            .also(EA.eventDispatcher)
    }
}
