package net.skymoe.enchaddons.event

interface CancelableEvent : Event {
    var canceled: Boolean
}
