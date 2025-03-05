package net.skymoe.enchaddons.impl.config

import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Switch
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.HypixelLocation
import net.skymoe.enchaddons.api.HypixelServerType
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.impl.EAImpl
import net.skymoe.enchaddons.impl.config.adapter.Extract
import net.skymoe.enchaddons.impl.config.announcement.DisclaimerAtOwnRisk
import net.skymoe.enchaddons.util.printChat

class MainConfig : ConfigImpl(featureInfo<FeatureConfig>("main", "General")) {
    @Transient
    @Extract
    val disclaimer = DisclaimerAtOwnRisk()

    @Transient
    @Header(
        text = "Flags",
        size = 2,
    )
    val headerFlag = false

    @Switch(
        name = "Disable EnchAddons Commands",
        size = 1,
    )
    var disableCommands = false

    @Switch(
        name = "Disable IBlockAccess Wrapping",
        size = 1,
    )
    var disableBlockAccess = false

    @Transient
    @Header(
        text = "Debug Flags",
        size = 2,
    )
    val headerDebugFlag = false

    @Switch(
        name = "Verbose Hypixel Mod API Wrapper",
        size = 1,
    )
    var verboseHypixelModAPI = false

    @Transient
    @Header(
        text = "Hypixel Mod API Location",
        size = 2,
    )
    val headerHypixelModAPILocation = false

    @Transient
    @Extract
    val printHypixelModAPILocation =
        @Button(
            name = "Print Hypixel Mod API Location",
            text = "Print",
            size = 1,
        )
        {
            printChat(EA.api.hypixelLocation.toString())
        }

    @Transient
    @Extract
    val setDungeon =
        @Button(
            name = "Set Dungeon",
            text = "Set",
            size = 1,
        )
        {
            EAImpl.api.hypixelLocation =
                HypixelLocation(
                    "mini0721KLOON",
                    serverType = HypixelServerType("SkyBlock"),
                    null,
                    "dungeon",
                    "Dungeon",
                )
        }
}
