package net.skymoe.enchaddons.impl.hud

import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.Feature
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.feature.ensureEnabled
import net.skymoe.enchaddons.impl.config.AdvancedHUD
import net.skymoe.enchaddons.impl.nanovg.GUIEvent
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.util.general.inBox

abstract class FeatureHUDBase<TO : FeatureConfig, TF : Feature<in TO>>(
    feature: TF,
    protected val hudGetter: FeatureHUDBase<TO, TF>.() -> AdvancedHUD,
) : FeatureGUIBase<TO, TF>(feature.inBox.cast()) {
    protected open val hud get() = hudGetter()
    protected open val example get() = hud.isExample
    override val scaledWidth get() = width * hud.scale
    override val scaledHeight get() = height * hud.scale
    override val transformation: Transformation
        get() = Transformation().scaleMC() translate hud.renderPos scale hud.renderScale

    override fun onRender(eventWidgets: MutableList<Widget<*>>) {
        val show = doesShow()
        val box = size
        val tr = transformation
        if (example) {
            if (show) {
                redraw(box, tr)
            }
            eventWidgets.addAll(widgets)
        } else {
            if (animation.updateScaleFadeState(show, box, tr, fadeOut)) {
                redraw(box, tr)
            }
            animation.scaleFadeWidgets(widgets, eventWidgets, getFadeOutOrigin(tr))
        }
    }

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        super.registerEvents(dispatcher)
        dispatcher.run {
            register<AdvancedHUD.GetWidthEvent> { event -> if (event.hud === hud) event.width = scaledWidth }
            register<AdvancedHUD.GetHeightEvent> { event -> if (event.hud === hud) event.height = scaledHeight }

            register<GUIEvent.HUD> { onRender(it.widgets) }
        }
    }

    protected open fun ensureHUDEnabled(frame: Int = 0) = ensureEnabled(frame) { hud.isEnabled }
}
