package net.skymoe.enchaddons.event.hypixel

import net.skymoe.enchaddons.event.Event

sealed interface SkyblockEvent : Event {
    data object ServerTick : SkyblockEvent
}
