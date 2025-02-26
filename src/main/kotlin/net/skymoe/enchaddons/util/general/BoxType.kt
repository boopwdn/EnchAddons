package net.skymoe.enchaddons.util.general

sealed interface BoxType<T> {
    val value: T

    @Suppress("UNCHECKED_CAST")
    fun <R> cast() = value as R
}

data class Box<T>(
    override val value: T,
): BoxType<T>

data class MutableBox<T>(
    override var value: T,
): BoxType<T>

inline val <T> T.inBox get() = Box(this)