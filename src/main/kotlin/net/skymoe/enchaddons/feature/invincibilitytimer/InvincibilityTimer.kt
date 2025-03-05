package net.skymoe.enchaddons.feature.invincibilitytimer

import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.hypixel.SkyblockEvent
import net.skymoe.enchaddons.event.minecraft.ChatEvent
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.featureInfo

val INVINCIBILITY_TIMER_INFO = featureInfo<InvincibilityTimerConfig>("invincibility_timer", "Invincibility Timer")

object InvincibilityTimer : FeatureBase<InvincibilityTimerConfig>(INVINCIBILITY_TIMER_INFO) {
    data class InvincibilityItem(
        val name: String,
        val invincibilityTicks: () -> Int,
        val coolDown: Int,
        val regex: Regex,
    )

    data class InvincibilityItemState(
        val itemInfo: InvincibilityItem,
        var invincibilityTicks: Int = itemInfo.invincibilityTicks(),
        var coolDown: Int = itemInfo.coolDown,
        var updateTime: Long = System.nanoTime(),
    )

    val itemStateList: MutableList<InvincibilityItemState> = mutableListOf()

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
            register<MinecraftEvent.LoadWorld.Post> {
                itemStateList.clear()
            }

            register<MinecraftEvent.Tick.Pre> {
                itemStateList.removeIf {
                    it.coolDown-- <= 0
                }
            }

            register<SkyblockEvent.ServerTick> {
                itemStateList.forEach {
                    if (it.invincibilityTicks > 0) it.invincibilityTicks--
                    it.updateTime = System.nanoTime()
                }
            }

            register<ChatEvent.Normal.Post> { event ->
                INVINCIBILITY_ITEMS.firstOrNull {
                    if (event.message.matches(it.regex)) {
                        itemStateList.add(InvincibilityItemState(it))
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }
}
