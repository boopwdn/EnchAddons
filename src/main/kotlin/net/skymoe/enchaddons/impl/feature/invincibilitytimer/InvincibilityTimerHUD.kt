package net.skymoe.enchaddons.impl.feature.invincibilitytimer

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.format
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotEvent
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotEvent.DynamicSpotElement
import net.skymoe.enchaddons.feature.ensureEnabled
import net.skymoe.enchaddons.feature.invincibilitytimer.InvincibilityTimer
import net.skymoe.enchaddons.impl.config.feature.InvincibilityTimerConfigImpl
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.nanovg.widget.ImageWidget
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

object InvincibilityTimerHUD {
    val configImpl by lazy { EA.getConfigImpl(InvincibilityTimerConfigImpl::class) }

    init {
        EA.eventDispatcher.run {
            register<DynamicSpotEvent.Render.Normal> { event ->
                longrun {
                    InvincibilityTimer.run {
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
                                    val leftText =
                                        if (isActive) {
                                            EA.api.format(configImpl.dynamicSpot.leftTextActive) {
                                                this["item"] = item
                                            }
                                        } else {
                                            EA.api.format(configImpl.dynamicSpot.leftTextCooldown) {
                                                this["item"] = item
                                            }
                                        }
                                    val rightText =
                                        if (isActive) {
                                            EA.api.format(configImpl.dynamicSpot.rightTextActive) {
                                                this["item"] = item
                                                this["time"] = "${(item.invincibilityTicks * 0.05).format(1)}s"
                                            }
                                        } else {
                                            EA.api.format(configImpl.dynamicSpot.rightTextCooldown) {
                                                this["item"] = item
                                                this["time"] = "${(item.coolDown * 0.05).format(1)}s"
                                            }
                                        }

                                    ImageWidget(
                                        tr pos Vec2D(.0, -1.0),
                                        tr size Vec2D(16.0, 16.0),
                                        item.itemInfo.image,
                                        1.0,
                                        tr size 0.0,
                                    ).also(widgets::add)

                                    TextWidget(
                                        leftText,
                                        tr pos Vec2D(20.0, .0),
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
                                        pos1 = tr pos Vec2D(20.0, 10.0),
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
                                            pos1 = tr pos Vec2D(20.0, 10.0),
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
}
