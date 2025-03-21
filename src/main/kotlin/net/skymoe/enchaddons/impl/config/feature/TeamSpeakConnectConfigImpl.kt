package net.skymoe.enchaddons.impl.config.feature

import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import net.skymoe.enchaddons.feature.teamspeakconnect.DynamicSpot
import net.skymoe.enchaddons.feature.teamspeakconnect.TEAMSPEAK_CONNECT_INFO
import net.skymoe.enchaddons.feature.teamspeakconnect.TeamSpeakConnectConfig
import net.skymoe.enchaddons.impl.config.ConfigImpl
import net.skymoe.enchaddons.impl.config.adapter.Extract
import net.skymoe.enchaddons.impl.config.announcement.DynamicSpotDependent

class TeamSpeakConnectConfigImpl :
    ConfigImpl(TEAMSPEAK_CONNECT_INFO),
    TeamSpeakConnectConfig {
    @Transient
    @Extract
    val dynamicSpotDependent = DynamicSpotDependent()

    @Text(
        name = "TeamSpeak Remote App Address",
        size = 2,
        subcategory = "General",
    )
    override var address: String = "ws://localhost:5899"

    @Text(
        name = "APIKey (Automatically filled by wrapper.)",
        size = 2,
        secure = true,
        subcategory = "General",
    )
    override var apiKey: String = ""

    class DynamicSpotImpl : DynamicSpot {
        @Switch(
            name = "Enabled",
            size = 2,
            subcategory = "Dynamic Spot",
        )
        var enabled: Boolean = true

        @Number(
            name = "Event display time (ticks)",
            size = 2,
            subcategory = "Dynamic Spot",
            min = 0F,
            max = 1000F,
        )
        override var displayTime: Int = 60

        @Switch(
            name = "Client Joined",
            size = 1,
            subcategory = "Dynamic Spot",
        )
        var clientJoinedEnabled = true

        @Text(
            name = "",
            size = 1,
            subcategory = "Dynamic Spot",
        )
        var clientJoinedText: String = "Client Joined"

        @Switch(
            name = "Client Left",
            size = 1,
            subcategory = "Dynamic Spot",
        )
        var clientLeftEnabled = true

        @Text(
            name = "",
            size = 1,
            subcategory = "Dynamic Spot",
        )
        var clientLeftText: String = "Client Left"

        @Text(
            name = "First Line",
            size = 2,
            subcategory = "Dynamic Spot",
        )
        var firstLine: String = "<event>"

        @Text(
            name = "Second Line",
            size = 2,
            subcategory = "Dynamic Spot",
        )
        var secondLine: String = "<name>"
    }

    @Extract
    override var dynamicSpot = DynamicSpotImpl()
}
