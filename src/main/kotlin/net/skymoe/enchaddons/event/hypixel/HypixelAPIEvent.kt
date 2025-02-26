package net.skymoe.enchaddons.event.hypixel

import net.skymoe.enchaddons.api.HypixelLocation
import net.skymoe.enchaddons.event.Event

sealed interface HypixelAPIEvent : Event {
    data class Location(
        val location: HypixelLocation?,
    ) : HypixelAPIEvent
}