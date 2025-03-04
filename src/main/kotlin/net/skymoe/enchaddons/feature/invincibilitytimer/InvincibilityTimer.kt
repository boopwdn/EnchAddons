package net.skymoe.enchaddons.feature.invincibilitytimer

import net.skymoe.enchaddons.api.Template
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.hypixel.SkyblockEvent
import net.skymoe.enchaddons.event.minecraft.ChatEvent
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotEvent
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotEvent.DynamicSpotElement
import net.skymoe.enchaddons.feature.ensureEnabled
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.nanovg.widget.TextWidget
import net.skymoe.enchaddons.impl.nanovg.widget.progressBarWidget
import net.skymoe.enchaddons.impl.oneconfig.fontMedium
import net.skymoe.enchaddons.util.Colors
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.double
import net.skymoe.enchaddons.util.math.format
import net.skymoe.enchaddons.util.partialTicks
import net.skymoe.enchaddons.util.scope.longrun
import kotlin.math.min

val INVINCIBILITY_TIMER_INFO = featureInfo<InvincibilityTimerConfig>("invincibility_timer", "Invincibility Timer")

object InvincibilityTimer : FeatureBase<InvincibilityTimerConfig>(INVINCIBILITY_TIMER_INFO) {
    fun setPlaceholders(template: Template) {
//        template["item"] = item
    }

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

    private val itemStateList: MutableList<InvincibilityItemState> = mutableListOf()

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
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

            register<DynamicSpotEvent.Render.Normal> { event ->
                longrun {
                    ensureEnabled()
//                    ensureSkyBlock()
                    if (itemStateList.isEmpty()) return@longrun

                    itemStateList.forEach { item ->
                        object : DynamicSpotElement() {
                            override val size: Vec2D = Vec2D(120F, 14F)
                            override val id: String
                                get() = "invincibility_timer:active:${item.itemInfo.name}"

                            override fun draw(
                                widgets: MutableList<Widget<*>>,
                                tr: Transformation,
                                dynamicSpotSize: Vec2D,
                            ) {
                                val isActive = item.invincibilityTicks > 0
//                                val leftText =
//                                    if (isActive) {
//                                        EA.api.format(config.dynamicSpotLeftTextActive, ::setPlaceholders)
//                                    } else {
//                                        EA.api.format(config.dynamicSpotLeftTextCooldown, ::setPlaceholders)
//                                    }
                                val leftText = item.itemInfo.name
                                val rightText =
                                    if (isActive) {
                                        "IN: ${(item.invincibilityTicks * 0.05).format(1)}s"
                                    } else {
                                        "CD: ${(item.coolDown * 0.05).format(1)}s"
                                    }

                                TextWidget(
                                    leftText,
                                    tr pos Vec2D(.0, .0),
                                    Colors.GRAY[0].rgb,
                                    tr size 8.0,
                                    fontMedium,
                                    Vec2D(.0, .0),
                                ).also(widgets::add)

                                TextWidget(
                                    rightText,
                                    tr pos Vec2D(dynamicSpotSize.x, .0),
                                    Colors.GRAY[0].rgb,
                                    tr size 8.0,
                                    fontMedium,
                                    Vec2D(1.0, .0),
                                ).also(widgets::add)

                                progressBarWidget(
                                    progress = (item.coolDown.double - partialTicks) / item.itemInfo.coolDown,
                                    pos1 = tr pos Vec2D(.0, 10.0),
                                    pos2 = tr pos Vec2D(dynamicSpotSize.x, 14.0),
                                    colorProgress = Colors.ORANGE[5].rgb,
                                    colorBackground = 0,
                                ).also(widgets::add)

                                if (isActive) {
                                    val time = System.nanoTime()

                                    progressBarWidget(
                                        progress =
                                            (
                                                item.invincibilityTicks -
                                                    min(
                                                        (time - item.updateTime) / 50_000_000.0,
                                                        1.0,
                                                    )
                                            ) /
                                                item.itemInfo.invincibilityTicks(),
                                        pos1 = tr pos Vec2D(.0, 10.0),
                                        pos2 = tr pos Vec2D(dynamicSpotSize.x, 14.0),
                                        colorProgress = Colors.GREEN[5].rgb,
                                        colorBackground = 0,
                                    ).also(widgets::add)
                                }
                            }
                        }.also(event.elements::add)
                    }
                }
            }
        }
    }
}
