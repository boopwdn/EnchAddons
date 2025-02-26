package net.skymoe.enchaddons.api

data class HypixelServerType(
    val name: String,
)

data class HypixelLocation(
    val serverName: String,
    val serverType: HypixelServerType?,
    val lobbyName: String?,
    val mode: String?,
    val map: String?,
)