package net.skymoe.enchaddons.impl.hypixel

import net.hypixel.modapi.HypixelModAPI
import net.skymoe.enchaddons.getLogger
import net.skymoe.enchaddons.util.scope.nothrow

private val logger = getLogger("Hypixel Mod Api Loader")

val loadHypixelModAPI by lazy {
    nothrow(logger::catching) {
        HypixelModAPIWrapper(HypixelModAPI.getInstance())
    }
}
