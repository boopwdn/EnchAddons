package net.skymoe.enchaddons.event.minecraft

import net.skymoe.enchaddons.event.Event

sealed interface ChatEvent : Event {
    sealed interface Normal : ChatEvent {
        val message: String

        data class Post(
            override val message: String,
        ) : Normal
    }
}
