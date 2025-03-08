package net.skymoe.enchaddons.util.general

import kotlinx.serialization.Serializable

sealed interface BoxType<out T> {
    val value: T

    @Suppress("UNCHECKED_CAST")
    fun <R> cast() = value as R
}

@Serializable
data class Box<out T>(
    override val value: T,
) : BoxType<T>

@Serializable
data class MutableBox<T>(
    override var value: T,
) : BoxType<T>

inline val <T> T.inBox get() = Box(this)

inline val <T> T.inMutableBox get() = MutableBox(this)

inline val <T> BoxType<T>.reBox get() = Box(value)

inline val <T> BoxType<T>.reMutableBox get() = MutableBox(value)
