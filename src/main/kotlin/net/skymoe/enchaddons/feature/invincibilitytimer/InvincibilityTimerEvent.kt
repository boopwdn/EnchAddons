package net.skymoe.enchaddons.feature.invincibilitytimer

import net.skymoe.enchaddons.event.Event

interface InvincibilityTimerEvent : Event {
    data class Procced(
        val item: InvincibilityTimer.InvincibilityItem,
    ) : InvincibilityTimerEvent

    data class CooldownEnd(
        val item: InvincibilityTimer.InvincibilityItem,
    ) : InvincibilityTimerEvent
}
