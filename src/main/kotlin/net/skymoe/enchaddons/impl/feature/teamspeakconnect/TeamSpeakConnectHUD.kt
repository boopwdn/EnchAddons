package net.skymoe.enchaddons.impl.feature.teamspeakconnect

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.format
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotEvent
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotEvent.DynamicSpotElement
import net.skymoe.enchaddons.feature.ensureEnabled
import net.skymoe.enchaddons.feature.teamspeakconnect.TeamSpeakConnect
import net.skymoe.enchaddons.impl.cache.getImageLoader
import net.skymoe.enchaddons.impl.config.feature.TeamSpeakConnectConfigImpl
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.nanovg.widget.ImageWidget
import net.skymoe.enchaddons.impl.nanovg.widget.TextWidget
import net.skymoe.enchaddons.impl.oneconfig.fontMedium
import net.skymoe.enchaddons.impl.oneconfig.fontSemiBold
import net.skymoe.enchaddons.util.Colors
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.scope.longrun

object TeamSpeakConnectHUD {
    val configImpl by lazy { EA.getConfigImpl(TeamSpeakConnectConfigImpl::class) }

    private val iconLoader = getImageLoader("images/teamspeak.png")

    init {
        EA.eventDispatcher.run {
            register<DynamicSpotEvent.Render.Normal> { event ->
                longrun {
                    TeamSpeakConnect.run {
                        ensureEnabled()
                        if (teamSpeakEvent.isEmpty()) return@longrun

                        teamSpeakEvent.forEach { tsEvent ->
                            object : DynamicSpotElement() {
                                override val size: Vec2D = Vec2D(100F, 16F)
                                override val id: String
                                    get() = "teamspeak_connect:${tsEvent.id}"

                                override fun draw(
                                    widgets: MutableList<Widget<*>>,
                                    tr: Transformation,
                                    dynamicSpotSize: Vec2D,
                                ) {
                                    val eventMessage =
                                        EA.api.format(
                                            when (tsEvent.type) {
                                                TeamSpeakConnect.TeamSpeakEventType.JOINED_CHANNEL ->
                                                    configImpl.dynamicSpot.clientJoinedText
                                                TeamSpeakConnect.TeamSpeakEventType.LEFT_CHANNEL ->
                                                    configImpl.dynamicSpot.clientLeftText
                                            },
                                        ) {
                                            this["name"] = tsEvent.name
                                        }

                                    val text1 =
                                        EA.api.format(configImpl.dynamicSpot.firstLine) {
                                            this["event"] = eventMessage
                                            this["name"] = tsEvent.name
                                        }

                                    val text2 =
                                        EA.api.format(configImpl.dynamicSpot.secondLine) {
                                            this["event"] = eventMessage
                                            this["name"] = tsEvent.name
                                        }

                                    ImageWidget(
                                        tr pos Vec2D(-2.0, -2.0),
                                        tr size Vec2D(20.0, 20.0),
                                        iconLoader,
                                        1.0,
                                        tr size 0.0,
                                    ).also(widgets::add)

                                    TextWidget(
                                        text1,
                                        tr pos Vec2D(21.0, .0),
                                        Colors.GRAY[0].rgb,
                                        tr size 8.0,
                                        fontMedium,
                                        Vec2D(.0, .0),
                                    ).also(widgets::add)

                                    TextWidget(
                                        text2,
                                        tr pos Vec2D(21.0, 10.0),
                                        Colors.GRAY[0].rgb,
                                        tr size 8.0,
                                        fontSemiBold,
                                        Vec2D(.0, .0),
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
