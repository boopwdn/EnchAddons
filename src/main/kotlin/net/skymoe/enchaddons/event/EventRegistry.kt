package net.skymoe.enchaddons.event

import kotlin.reflect.KClass

interface EventRegistry {
    sealed interface Entry : (EventDispatcher) -> Unit {
        val handler: EventHandler<*>
    }

    val eventEntries: List<Entry>
}

class RegistryEventDispatcher(
    private val list: MutableList<EventRegistry.Entry>,
) : EventDispatcher {
    private data class Register<T : Event>(
        val type: KClass<T>,
        val priority: Int,
        override val handler: EventHandler<T>,
    ) : EventRegistry.Entry {
        override fun invoke(dispatcher: EventDispatcher) {
            dispatcher.register(type, priority, handler)
        }
    }

    private data class RegisterOnly<T : Event>(
        val type: KClass<T>,
        val priority: Int,
        override val handler: EventHandler<T>,
    ) : EventRegistry.Entry {
        override fun invoke(dispatcher: EventDispatcher) {
            dispatcher.registerOnly(type, priority, handler)
        }
    }

    override fun <T : Event> register(
        type: KClass<T>,
        priority: Int,
        handler: EventHandler<T>,
    ) {
        list.add(Register(type, priority, handler))
    }

    override fun <T : Event> registerOnly(
        type: KClass<T>,
        priority: Int,
        handler: EventHandler<T>
    ) {
        list.add(RegisterOnly(type, priority, handler))
    }

    override fun <T : Event> unregister(
        type: KClass<T>,
        handler: EventHandler<T>
    ) = throw NotImplementedError()

    override fun <T : Event> unregisterOnly(
        type: KClass<T>,
        handler: EventHandler<T>
    ) = throw NotImplementedError()

    override fun clear() = throw NotImplementedError()

    override fun unregisterAll(handler: EventHandler<*>) = throw NotImplementedError()

    override fun <T : Event> getHandler(type: KClass<T>) = throw NotImplementedError()

    override fun <T : Event> getHandlerOnly(type: KClass<T>) = throw NotImplementedError()
}

fun buildEventEntries(function: RegistryEventDispatcher.() -> Unit): List<EventRegistry.Entry> {
    return mutableListOf<EventRegistry.Entry>().also { function(RegistryEventDispatcher(it)) }
}

fun EventRegistry.buildRegisterEventEntries(
    dispatcher: EventDispatcher,
    function: RegistryEventDispatcher.() -> Unit,
): List<EventRegistry.Entry> {
    return mutableListOf<EventRegistry.Entry>()
        .also { function(RegistryEventDispatcher(it)) }
        .also { registerEventEntries(dispatcher) }
}

fun EventRegistry.registerEventEntries(dispatcher: EventDispatcher) {
    eventEntries.registerEventEntries(dispatcher)
}

fun EventRegistry.unregisterEventEntries(dispatcher: EventDispatcher) {
    eventEntries.unregisterEventEntries(dispatcher)
}

fun List<EventRegistry.Entry>.registerEventEntries(dispatcher: EventDispatcher) {
    forEach { it(dispatcher) }
}

fun List<EventRegistry.Entry>.unregisterEventEntries(dispatcher: EventDispatcher) {
    val set = mutableSetOf<EventHandler<*>>()
    forEach { set.add(it.handler) }
    set.forEach(dispatcher::unregisterAll)
}
