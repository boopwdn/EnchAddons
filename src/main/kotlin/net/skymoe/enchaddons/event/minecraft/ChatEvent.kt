package net.skymoe.enchaddons.event.minecraft

import net.skymoe.enchaddons.event.Event

sealed interface ChatEvent : Event {
    sealed interface Normal : ChatEvent {
        val message: String
        val messageRaw: String

        data class Pre(
            override val message: String,
            override val messageRaw: String,
        ) : Normal
    }
}
