package net.skymoe.enchaddons.impl.nanovg

import net.skymoe.enchaddons.util.math.ExponentialAnimation
import net.skymoe.enchaddons.util.math.Vec2D

class WidgetAnimation {
    private var lastShow = false
    private val smoothAlpha = ExponentialAnimation(0.0)
    private val smoothScale = ExponentialAnimation(0.0)
    val smoothY = ExponentialAnimation(0.0)
    private var lastShowFadeOut = false
    private var lastSwitch: Long? = null

    fun doFadeOutUpdate(show: Boolean): Boolean {
        if (show) {
            if (!lastShow) {
                smoothAlpha.set(0.1)
                smoothScale.set(0.9)
            }
            if (smoothAlpha.approach(1.5, 0.5) > 0.9999) {
                smoothAlpha.set(1.0)
            }
            if (smoothScale.approach(1.1, 0.5) > 0.9999) {
                smoothScale.set(1.0)
            }
        } else {
            if (lastShow) {
                smoothAlpha.set(1.0)
                smoothScale.set(1.0)
            }
            smoothAlpha.approach(-0.5, 0.5)
            smoothScale.approach(0.8, 0.5)
        }
        lastShow = show
        return smoothAlpha.value >= 0.1
    }

    fun update(
        show: Boolean,
        fadeOut: Long = 0,
    ): Boolean {
        val time = System.nanoTime()
        var toUpdate = true
        if (!show && fadeOut > 0) {
            if (lastShowFadeOut) {
                lastSwitch = time
                toUpdate = false
            } else if (lastSwitch?.let { time - it < fadeOut } == true) {
                toUpdate = false
            }
        }
        lastShowFadeOut = show
        return toUpdate and doFadeOutUpdate(show || !toUpdate)
    }

    fun mapWidgets(
        widgets: List<Widget<*>>,
        eventWidgets: MutableList<Widget<*>>,
        fadeOutOrigin: Vec2D,
    ) {
        if (smoothAlpha.value > 0.1) {
            eventWidgets.addAll(
                widgets.map {
                    it
                        .alphaScale(smoothAlpha.value)
                        .scale(smoothScale.value, fadeOutOrigin)
                },
            )
        }
    }

    fun setShow(show: Boolean) {
        if (show) {
            smoothAlpha.set(1.0)
            smoothScale.set(1.0)
        } else {
            smoothAlpha.set(0.0)
            smoothScale.set(0.9)
        }
        lastSwitch = null
        lastShow = show
        lastShowFadeOut = show
    }
}
