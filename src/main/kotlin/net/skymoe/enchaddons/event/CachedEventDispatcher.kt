package net.skymoe.enchaddons.event

import net.skymoe.enchaddons.util.general.inBox
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class CachedEventDispatcher(
    private val dispatcher: EventDispatcher,
) : EventDispatcher by dispatcher {
    private val handlerCache = ConcurrentHashMap<KClass<*>, EventHandler<*>>()
    private val handlerOnlyCache = ConcurrentHashMap<KClass<*>, EventHandler<*>>()

    fun clearCache() {
        handlerCache.clear()
        handlerOnlyCache.clear()
    }

    override fun <T : Event> getHandler(type: KClass<T>): EventHandler<T> =
        handlerCache
            .getOrPut(type) {
                dispatcher.getHandler(type)
            }.inBox
            .cast()

    override fun <T : Event> getHandlerOnly(type: KClass<T>): EventHandler<T> =
        handlerOnlyCache
            .getOrPut(type) {
                dispatcher.getHandlerOnly(type)
            }.inBox
            .cast()
}
