package net.skymoe.enchaddons.util.property

import net.skymoe.enchaddons.util.general.Box
import net.skymoe.enchaddons.util.general.inBox
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LateLet<T> : ReadWriteProperty<Any?, T> {
    private var box: Box<T>? = null

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T {
        return box?.cast() ?: throw IllegalStateException()
    }

    override fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: T,
    ) {
        box?.let { throw IllegalStateException() }
        box = value.inBox
    }
}

fun <T> latelet() = LateLet<T>()
