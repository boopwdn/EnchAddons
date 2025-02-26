package net.skymoe.enchaddons.util

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.skymoe.enchaddons.getLogger

private val logger = getLogger("MinecraftUtil")

val MC: Minecraft by lazy { Minecraft.getMinecraft() }

fun printChat(message: String = "") {
    logger.info("[CHAT] $message")
    MC.theWorld?.let {
        MC.ingameGUI.chatGUI.printChatMessage(ChatComponentText(message))
    }
}

fun printChat(throwable: Throwable) = printChat(throwable.stackTraceMessage)