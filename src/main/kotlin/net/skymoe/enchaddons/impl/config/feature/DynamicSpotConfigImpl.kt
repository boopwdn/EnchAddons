package net.skymoe.enchaddons.impl.config.feature

import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import net.skymoe.enchaddons.feature.dynamicspot.DYNAMIC_SPOT_INFO
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpotConfig
import net.skymoe.enchaddons.impl.config.AdvancedHUD
import net.skymoe.enchaddons.impl.config.ConfigImpl
import net.skymoe.enchaddons.impl.config.adapter.Extract
import net.skymoe.enchaddons.impl.config.gui.GUIBackground

class DynamicSpotConfigImpl :
    ConfigImpl(DYNAMIC_SPOT_INFO),
    DynamicSpotConfig {
    @HUD(
        name = "Dynamic Spot",
    )
    var hud = AdvancedHUD()

    @Text(
        name = "Content",
        size = 2,
        subcategory = "Idle state",
    )
    var idleText = "EA | <fps> FPS"

    @Slider(
        name = "Width",
        min = 1.0F,
        max = 200F,
        subcategory = "Idle state",
        instant = true,
    )
    var idleTextWidth = 52F

    @Slider(
        name = "Height",
        min = 1.0F,
        max = 200F,
        subcategory = "Idle state",
        instant = true,
    )
    var idleTextHeight = 6F

    @Extract
    var background = GUIBackground()

    @Switch(
        name = "Force Example",
        size = 1,
    )
    var forceExample = false
}
