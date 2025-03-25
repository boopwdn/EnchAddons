package net.skymoe.enchaddons.util

import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

fun String.asComponent(init: IChatComponent.() -> Unit = {}) = ChatComponentText(this).also(init)

var IChatComponent.command: String?
    get() = this.chatStyle.chatClickEvent?.let { if (it.action == ClickEvent.Action.RUN_COMMAND) it.value else null }
    set(value) {
        this.chatStyle.chatClickEvent = value?.let { ClickEvent(ClickEvent.Action.RUN_COMMAND, it) }
    }

var IChatComponent.hover: IChatComponent?
    get() = this.chatStyle.chatHoverEvent?.let { if (it.action == HoverEvent.Action.SHOW_TEXT) it.value else null }
    set(value) {
        this.chatStyle.chatHoverEvent = value?.let { HoverEvent(HoverEvent.Action.SHOW_TEXT, it) }
    }
