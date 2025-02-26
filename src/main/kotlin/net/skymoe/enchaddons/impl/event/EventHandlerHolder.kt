package net.skymoe.enchaddons.impl.event

import net.skymoe.enchaddons.event.Event
import net.skymoe.enchaddons.event.EventHandler

class EventHandlerHolder<in T : Event> (
    private val handlers: List<EventHandler<T>>,
) : EventHandler<T> {
    override fun invoke(event: T) = handlers.forEach { it(event) }
}