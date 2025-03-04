package net.skymoe.enchaddons.impl.hud

import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.feature.Feature
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.nanovg.WindowAnimation
import net.skymoe.enchaddons.util.general.inBox
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.scope.longrun

abstract class FeatureGUIBase<TO : FeatureConfig, TF : Feature<in TO>>(
    protected val feature: TF,
) : FeatureBase<TO>(feature.inBox.cast()) {
    protected abstract val width: Double
    protected abstract val height: Double
    protected open val fadeOut = 0L

    protected abstract fun ensureShow()

    protected abstract fun draw(
        widgets: MutableList<Widget<*>>,
        box: Vec2D,
        tr: Transformation,
    )

    protected open fun doesShow(): Boolean {
        var show = false
        longrun {
            ensureShow()
            show = true
        }

        return show
    }

    protected open fun reset() {}

    protected open val scaledWidth get() = width
    protected open val scaledHeight get() = height
    protected open val size get() = Vec2D(width, height)
    protected abstract val transformation: Transformation
    protected open val widgets = mutableListOf<Widget<*>>()
    protected open val animation = WindowAnimation()

    protected open fun getFadeOutOrigin(tr: Transformation) = tr pos size / 2.0

    protected open fun redraw(
        box: Vec2D,
        tr: Transformation,
    ) {
        widgets.clear()
        draw(widgets, box, tr)
    }

    protected open fun onRender(eventWidgets: MutableList<Widget<*>>) {
        val show = doesShow()
        val box = size
        val tr = transformation
        if (animation.updateScaleFadeState(show, box, tr, fadeOut)) redraw(box, tr)
        animation.scaleFadeWidgets(widgets, eventWidgets, getFadeOutOrigin(tr))
        if (!doesShow()) reset()
    }

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
    }
}
