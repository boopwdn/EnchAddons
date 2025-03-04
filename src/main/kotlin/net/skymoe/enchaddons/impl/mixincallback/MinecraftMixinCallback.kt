package net.skymoe.enchaddons.impl.mixincallback

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent

fun startGamePost() {
}

fun onRunTickPre() {
    MinecraftEvent.Tick.Pre
        .also(EA.eventDispatcher)
}
