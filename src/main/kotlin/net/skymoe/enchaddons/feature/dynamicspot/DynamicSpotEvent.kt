package net.skymoe.enchaddons.feature.dynamicspot

import net.skymoe.enchaddons.event.Event
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.util.math.Vec2D

sealed interface DynamicSpotEvent : Event {
    abstract class DynamicSpotElement {
        abstract val size: Vec2D
        var isFadeOut = false
        abstract val id: String

        abstract fun draw(
            widgets: MutableList<Widget<*>>,
            tr: Transformation,
            dynamicSpotSize: Vec2D,
        )

        override fun hashCode(): Int = id.hashCode()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass !== other?.javaClass) return false
            other as DynamicSpotElement
            return hashCode() == other.hashCode()
        }
    }

    sealed interface Render : DynamicSpotEvent {
        data class Normal(
            val elements: MutableList<DynamicSpotElement> = mutableListOf(),
        ) : Render

        data class Idle(
            val elements: MutableList<DynamicSpotElement> = mutableListOf(),
        ) : Render
    }
}
