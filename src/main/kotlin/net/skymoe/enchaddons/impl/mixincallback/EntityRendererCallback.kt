package net.skymoe.enchaddons.impl.mixincallback

import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.minecraft.RenderEvent
import net.skymoe.enchaddons.impl.nanovg.GUIEvent
import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.glStateScope
import net.skymoe.enchaddons.util.partialTicks

object EntityRendererCallback {
    object General {
        fun updateCameraAndRenderPre(partialTicksIn: Double) {
            partialTicks = partialTicksIn
            EA.eventDispatcher(RenderEvent.Render.Pre)
        }

        fun updateCameraAndRenderRenderHUD() {
            GUIEvent.HUD
                .Post()
                .also(EA.eventDispatcher)
                .apply {
                    glStateScope {
                        val helper = NanoVGHelper.INSTANCE
                        helper.setupAndDraw { vg ->
                            val context = NanoVGUIContext(helper, vg)
                            helper.setAlpha(vg, 1.0F)
                            widgets.forEach { it.draw(context) }
                        }
                    }
                }
        }

        fun updateCameraAndRenderProxyScreenAtCondition() {
            if (MC.currentScreen !== null) return
            GUIEvent.Screen
                .Post()
                .also(EA.eventDispatcher)
                .apply {
                    glStateScope {
                        val helper = NanoVGHelper.INSTANCE
                        helper.setupAndDraw { vg ->
                            val context = NanoVGUIContext(helper, vg)
                            helper.setAlpha(vg, 1.0F)
                            widgets.forEach { it.draw(context) }
                        }
                    }
                }
        }
    }
}
