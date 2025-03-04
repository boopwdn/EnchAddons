package net.skymoe.enchaddons.event

import net.skymoe.enchaddons.util.general.inBox
import kotlin.reflect.KClass

interface EventDispatcher : (Event) -> Unit {
    /*
     * 为直接继承的两个类注册相同的 Handler 是未定义行为
     * 如果使用相同参数多次调用该方法，则只有第一次调用才会生效
     */
    fun <T : Event> register(
        type: KClass<T>,
        priority: Int = 0,
        handler: EventHandler<T>,
    )

    fun <T : Event> registerOnly(
        type: KClass<T>,
        priority: Int = 0,
        handler: EventHandler<T>,
    )

    fun <T : Event> unregister(
        type: KClass<T>,
        handler: EventHandler<T>,
    )

    fun <T : Event> unregisterOnly(
        type: KClass<T>,
        handler: EventHandler<T>,
    )

    fun unregisterAll(handler: EventHandler<*>)

    fun clear()

    fun <T : Event> getHandler(type: KClass<T>): EventHandler<T>
    fun <T : Event> getHandler(event: T) = getHandler(event::class.inBox.cast<KClass<T>>())
    fun <T : Event> getHandlerOnly(type: KClass<T>): EventHandler<T>
    fun <T : Event> getHandlerOnly(event: T) = getHandlerOnly(event::class.inBox.cast<KClass<T>>())

    override fun invoke(event: Event) {
        getHandler(event)(event)
    }
}

inline fun <reified T : Event> EventDispatcher.register(
    priority: Int = 0,
    noinline handler: EventHandler<T>,
) = register(T::class, priority, handler)

inline fun <reified T : Event> EventDispatcher.registerOnly(
    priority: Int = 0,
    noinline handler: EventHandler<T>,
) = register(T::class, priority, handler)

inline fun <reified T : Event> EventDispatcher.unregisterOnly(noinline handler: EventHandler<T>) = unregisterOnly(T::class, handler)

inline fun <reified T : Event> EventDispatcher.getHandler() = getHandler(T::class)

inline fun <reified T : Event> EventDispatcher.getHandlerOnly() = getHandlerOnly(T::class)