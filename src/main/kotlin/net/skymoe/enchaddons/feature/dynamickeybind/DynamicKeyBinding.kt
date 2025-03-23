package net.skymoe.enchaddons.feature.dynamickeybind

import kotlinx.atomicfu.atomic
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.util.math.ExponentialAnimation

val DYNAMIC_KEYBINDING_INFO = featureInfo<DynamicKeyBindingConfig>("dynamic_keybinding", "Dynamic KeyBinding")

object DynamicKeyBinding : FeatureBase<DynamicKeyBindingConfig>(DYNAMIC_KEYBINDING_INFO) {
    private val atomicInt = atomic(0)

    data class KeyBindingState(
        var aliveTicks: Int,
        val totalTicks: Int,
        val id: String,
        val action: () -> Unit,
        val actionText: String,
        val keyAnimation: MutableMap<Int, ExponentialAnimation> = mutableMapOf(),
    )

    val keyBindingList: MutableList<KeyBindingState> = mutableListOf()

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
            register<MinecraftEvent.Tick.Pre> {
                keyBindingList.removeIf {
                    --it.aliveTicks <= 0
                }
            }

//            register<ChatEvent.Normal.Post> {
//                DynamicKeyBindingEvent
//                    .Add(
//                        object : DynamicKeyBindingEvent.DynamicKeyBindingElement(
//                            aliveTime = 100,
//                            id = "test",
//                            action = {
//                                printChat("tested")
//                            },
//                            actionText = "Get a draft",
//                        ) {},
//                    ).also(EA.eventDispatcher)
//            }

            register<DynamicKeyBindingEvent.Add> { event ->
                keyBindingList.add(
                    KeyBindingState(
                        aliveTicks = event.element.aliveTime,
                        totalTicks = event.element.aliveTime,
                        id = "${event.element.id}:${atomicInt.getAndIncrement()}",
                        action = event.element.action,
                        actionText = event.element.actionText,
                    ),
                )
            }

            register<DynamicKeyBindingEvent.KEYPRESS.MAIN> {
                synchronized(keyBindingList) {
                    keyBindingList.getOrNull(0)?.let { state ->
                        state.action()
                        keyBindingList.removeAt(0)
                    }
                }
            }

            register<DynamicKeyBindingEvent.KEYPRESS.SECOND> {
                synchronized(keyBindingList) {
                    keyBindingList.getOrNull(1)?.let { state ->
                        state.action()
                        keyBindingList.removeAt(1)
                    }
                }
            }
        }
    }
}
