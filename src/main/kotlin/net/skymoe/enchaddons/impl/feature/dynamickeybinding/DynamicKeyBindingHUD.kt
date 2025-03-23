package net.skymoe.enchaddons.impl.feature.dynamickeybinding

import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.format
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.dynamickeybind.DynamicKeyBinding
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotEvent
import net.skymoe.enchaddons.feature.ensureEnabled
import net.skymoe.enchaddons.impl.config.feature.DynamicKeyBindingConfigImpl
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.nanovg.widget.KeyWidget
import net.skymoe.enchaddons.impl.nanovg.widget.TextWidget
import net.skymoe.enchaddons.impl.oneconfig.fontMedium
import net.skymoe.enchaddons.impl.oneconfig.fontSemiBold
import net.skymoe.enchaddons.util.general.inMutableBox
import net.skymoe.enchaddons.util.math.ExponentialAnimation
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.partialTicks
import net.skymoe.enchaddons.util.scope.longrun

object DynamicKeyBindingHUD {
    val configImpl by lazy { EA.getConfigImpl(DynamicKeyBindingConfigImpl::class) }

    init {
        EA.eventDispatcher.run {
            register<DynamicSpotEvent.Render.Normal> { event ->
                longrun {
                    DynamicKeyBinding.run {
                        ensureEnabled()

                        if (keyBindingList.isEmpty()) return@longrun

                        keyBindingList.indices.forEach { i ->
                            val state = keyBindingList[i]

                            object : DynamicSpotEvent.DynamicSpotElement() {
                                override val size: Vec2D = Vec2D(120F, 16F)
                                override val id: String
                                    get() = "dynamic_keybinding:${state.id}}"

                                override fun draw(
                                    widgets: MutableList<Widget<*>>,
                                    tr: Transformation,
                                    dynamicSpotSize: Vec2D,
                                ) {
                                    val trBox = Transformation().inMutableBox

                                    val keyCodeList =
                                        when (i) {
                                            0 -> configImpl.mainActionKey.keyBinds
                                            1 -> configImpl.secondActionKey.keyBinds
                                            else -> listOf(UKeyboard.KEY_NONE)
                                        }

                                    keyCodeList.indices.forEach { j ->
                                        KeyWidget(
                                            keyCodeList[j],
                                            tr pos Vec2D(.0, .0),
                                            tr size 16.0,
                                            if (j == 0) 1.0 * (state.aliveTicks - partialTicks) / state.totalTicks else 0.0,
                                            tr size 1.2,
                                            tr size 4.0,
                                            tr size 8.0,
                                            tr size 8.0,
                                            fontSemiBold,
                                            configImpl.keyBackgroundColor.rgb,
                                            configImpl.keyTextColor.rgb,
                                            configImpl.countDownProgressRingColor.rgb,
                                            trBox = trBox,
                                            animation = state.keyAnimation.getOrPut(j) { ExponentialAnimation(0.0) },
                                        ) { width ->
                                            trBox.value += Vec2D(width, 0.0)
                                            trBox.value += Vec2D(tr size 5.0, 0.0)
                                        }.also(widgets::add)
                                    }

                                    TextWidget(
                                        configImpl.firstLineText,
                                        tr pos Vec2D(.0, .0),
                                        configImpl.firstLineTextColor.rgb,
                                        tr size 8.0,
                                        fontMedium,
                                        Vec2D(.0, .0),
                                        trBox = trBox,
                                    ).also(widgets::add)

                                    TextWidget(
                                        EA.api.format(configImpl.secondLineText) {
                                            this["action"] = state.actionText
                                        },
                                        tr pos Vec2D(.0, 9.0),
                                        configImpl.secondLineTextColor.rgb,
                                        tr size 8.0,
                                        fontSemiBold,
                                        Vec2D(.0, .0),
                                        trBox = trBox,
                                    ).also(widgets::add)
                                }
                            }.also(event.elements::add)
                        }
                    }
                }
            }
        }
    }
}
