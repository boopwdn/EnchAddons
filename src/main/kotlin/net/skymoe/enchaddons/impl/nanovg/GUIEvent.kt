package net.skymoe.enchaddons.impl.nanovg

import net.skymoe.enchaddons.event.Event

sealed interface GUIEvent : Event {
    val widgets: MutableList<Widget<*>>

    sealed interface HUD : GUIEvent {
        data class Post(
            override val widgets: MutableList<Widget<*>> = mutableListOf(),
        ) : HUD
    }

    sealed interface Screen : GUIEvent {
        data class Post(
            override val widgets: MutableList<Widget<*>> = mutableListOf(),
        ) : Screen
    }
}