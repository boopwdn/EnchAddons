package net.skymoe.enchaddons.impl.mixincallback

import net.minecraft.client.multiplayer.WorldClient
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent

fun onWorldLoadPost(worldClient: WorldClient) {
    MinecraftEvent.LoadWorld
        .Post(worldClient)
        .also(EA.eventDispatcher)
}
