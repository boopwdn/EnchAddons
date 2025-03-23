package net.skymoe.enchaddons.feature.dynamickeybind

import net.skymoe.enchaddons.event.Event

sealed interface DynamicKeyBindingEvent : Event {
    abstract class DynamicKeyBindingElement(
        val aliveTime: Int,
        val id: String,
        val action: () -> Unit,
        val actionText: String,
    )

    data class Add(
        val element: DynamicKeyBindingElement,
    ) : DynamicKeyBindingEvent

    sealed interface KEYPRESS : DynamicKeyBindingEvent {
        data object MAIN : KEYPRESS

        data object SECOND : KEYPRESS
    }
}
