package net.skymoe.enchaddons.impl.mixincallback

import net.minecraft.client.multiplayer.WorldClient
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent

fun startGamePost() {
}

fun onRunTickPre() {
    MinecraftEvent.Tick.Pre
        .also(EA.eventDispatcher)
}

fun onWorldUnloadPre(world: WorldClient) {
    MinecraftEvent.World.Unload
        .Pre(world)
        .also(EA.eventDispatcher)
}
