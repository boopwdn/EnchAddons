package net.skymoe.enchaddons.impl.mixincallback

import net.minecraft.client.multiplayer.WorldClient
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.util.tickCounter

fun startGamePost() {
}

fun onRunTickPre() {
    tickCounter++
    MinecraftEvent.Tick.Pre
        .also(EA.eventDispatcher)
}

fun onWorldUnloadPre(world: WorldClient) {
    MinecraftEvent.World.Unload
        .Pre(world)
        .also(EA.eventDispatcher)
}
