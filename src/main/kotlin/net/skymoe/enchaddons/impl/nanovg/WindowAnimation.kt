package net.skymoe.enchaddons.impl.nanovg

import net.skymoe.enchaddons.util.math.ExponentialAnimation
import net.skymoe.enchaddons.util.math.Vec2D

class WindowAnimation {
    private var lastShow = false
    private var smoothAlpha = ExponentialAnimation(.0)
    private var smoothScale = ExponentialAnimation(.0)

    private data object SmoothSize {
        val x: ExponentialAnimation = ExponentialAnimation(.0)
        val y: ExponentialAnimation = ExponentialAnimation(.0)
    }

    private var box = Vec2D(0.0, 0.0)
    private var tr = Transformation()
    private var lastShowFadeOut = false
    private var lastSwitch: Long? = null

    private fun updateAlphaAndScale(show: Boolean): Boolean {
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
        return show
    }

    fun sizeAnimate(sizeTargetIn: Vec2D): Vec2D {
        SmoothSize.x.approach(sizeTargetIn.x, 0.5)
        SmoothSize.y.approach(sizeTargetIn.y, 0.5)
        return Vec2D(SmoothSize.x.value, SmoothSize.y.value)
    }

    // Returns a boolean that indicated can stop render
    fun updateScaleFadeState(
        show: Boolean,
        box: Vec2D,
        tr: Transformation,
        fadeOut: Long = 0,
    ): Boolean {
        val time = System.nanoTime()
        var fadedOut = true
        this.box = box
        this.tr = tr
        if (!show && fadeOut > 0) {
            if (lastShowFadeOut) {
                lastSwitch = time
                fadedOut = false
            } else if (lastSwitch?.let { time - it < fadeOut } == true) {
                fadedOut = false
            }
        }
        lastShowFadeOut = show
        return fadedOut and updateAlphaAndScale(show || !fadedOut)
    }

    fun scaleFadeWidgets(
        widgetsIn: List<Widget<*>>,
        widgetsOut: MutableList<Widget<*>>,
        fadeOutOrigin: Vec2D,
    ) {
        if (smoothAlpha.value > 0.1) {
            widgetsOut.addAll(
                widgetsIn.map {
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
