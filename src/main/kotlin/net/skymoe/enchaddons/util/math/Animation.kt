package net.skymoe.enchaddons.util.math

import kotlin.math.pow

class NoneAnimation

class ExponentialAnimation(
    valueIn: Double,
) {
    var value = valueIn
        private set

    private var last: Long? = null

    val initialized get() = last !== null

    fun approach(
        target: Double,
        speed: Double,
    ): Double {
        val time = System.nanoTime()
        val diff = time - (last ?: time)
        last = time
        value = target - (target - value) * (1.0 - speed).pow(diff / 50_000_000.0)
        return value
    }

    fun approachOrSet(
        target: Double,
        speed: Double,
    ): Double {
        val time = System.nanoTime()
        val diff = time - (last ?: time)
        last?.let {
            value = target - (target - value) * (1.0 - speed).pow(diff / 50000000.0)
        } ?: run { value = target }
        last = time
        return value
    }

    fun set(target: Double) {
        value = target
        last = System.nanoTime()
    }

    fun setValue(target: Double) {
        value = target
    }

    fun reset(target: Double) {
        value = target
        last = null
    }
}
