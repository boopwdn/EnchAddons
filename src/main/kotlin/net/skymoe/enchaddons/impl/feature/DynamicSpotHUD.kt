package net.skymoe.enchaddons.impl.feature

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.format
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpot
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotEvent
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotEvent.DynamicSpotElement
import net.skymoe.enchaddons.impl.config.feature.DynamicSpotConfigImpl
import net.skymoe.enchaddons.impl.hud.FeatureHUDBase
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.nanovg.WidgetAnimation
import net.skymoe.enchaddons.impl.nanovg.widget.TextWidget
import net.skymoe.enchaddons.impl.oneconfig.fontMedium
import net.skymoe.enchaddons.util.Colors
import net.skymoe.enchaddons.util.math.Vec2D

object DynamicSpotHUD : FeatureHUDBase<DynamicSpotConfigImpl, DynamicSpot>(DynamicSpot, { config.hud }) {
    data object NowSize {
        var width = .0
        var height = .0

        fun toVec2D(): Vec2D = Vec2D(width, height)
    }

    private var targetSize: Vec2D = Vec2D(.0, .0)

    override val width: Double
        get() = NowSize.width
    override val height: Double
        get() = NowSize.height

    override val fadeOut = 3000000000L

    override val example get() = super.example || config.forceExample

    override fun ensureShow() {
        ensureHUDEnabled()
    }

    private var elementsRegistry: MutableList<DynamicSpotElement> = mutableListOf()
    private var animationsMap: HashMap<DynamicSpotElement, WidgetAnimation> = hashMapOf()

    private const val GAP = 4.0

    private fun updateRegistry(elements: MutableList<DynamicSpotElement>) {
        elementsRegistry =
            (
                elementsRegistry.map { element ->
                    elements.find(element::equals) ?: element
                } +
                    elements.filter {
                        it !in elementsRegistry
                    }
            ).toMutableList()

        elementsRegistry.filter { it !in elements }.forEach { it.isFadeOut = true }
    }

    override fun draw(
        widgets: MutableList<Widget<*>>,
        box: Vec2D,
        tr: Transformation,
    ) {
        val elements =
            DynamicSpotEvent.Render
                .Normal()
                .also(EA.eventDispatcher)
                .elements
                .takeUnless { it.isEmpty() }
                ?: DynamicSpotEvent.Render
                    .Idle()
                    .also(EA.eventDispatcher)
                    .elements

        updateRegistry(elements)

        config.background.addTo(widgets, tr, NowSize.toVec2D())

        val dynamicSpotWidth = elementsRegistry.maxOf { it.size.x }
        val dynamicSpotHeight = elementsRegistry.sumOf { it.size.y }

        targetSize = Vec2D(dynamicSpotWidth, dynamicSpotHeight + (elementsRegistry.size - 1) * GAP)

        var currentY = 0.0

        elementsRegistry.removeIf { element ->
            val widgetsNow: MutableList<Widget<*>> = mutableListOf()

            animationsMap
                .getOrPut(element, ::WidgetAnimation)
                .let { animation ->
                    if (!animation.smoothY.initialized) {
                        animation.smoothY.set(currentY + GAP)
                    }

                    val smoothY = animation.smoothY.approach(currentY, 0.35)
                    val smoothTR = tr + Vec2D(0.0, smoothY)

                    element.draw(widgetsNow, smoothTR, NowSize.toVec2D())

                    currentY += element.size.y
                    currentY += 4.0

                    animation
                        .doFadeOutUpdate(!element.isFadeOut)
                        .also {
                            animation.mapWidgets(
                                widgetsNow,
                                widgets,
                                smoothTR pos NowSize.toVec2D() / 2.0,
                            )
                        }
                }.let { !it }
        }

        animationsMap.entries.removeIf {
            it.key !in elementsRegistry
        }

        animation
            .sizeAnimate(targetSize)
            .also {
                NowSize.width = it.x
                NowSize.height = it.y
            }
    }

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        super.registerEvents(dispatcher)
        dispatcher.run {
            register<DynamicSpotEvent.Render.Idle> { event ->
                object : DynamicSpotElement() {
                    override fun draw(
                        widgets: MutableList<Widget<*>>,
                        tr: Transformation,
                        dynamicSpotSize: Vec2D,
                    ) {
                        val idleText = EA.api.format(config.idleText, feature::setPlaceholders)

                        TextWidget(
                            idleText,
                            tr pos Vec2D(width / 2, .0),
                            Colors.GRAY[0].rgb,
                            tr size 8.0,
                            fontMedium,
                            Vec2D(.5, .0),
                        ).also { widgets.add(it) }
                    }

                    override val size: Vec2D
                        get() = Vec2D(config.idleTextWidth, config.idleTextHeight)
                    override val id: String
                        get() = "dynamic_spot:idle"
                }.also { event.elements.add(it) }
            }
        }
    }
}
