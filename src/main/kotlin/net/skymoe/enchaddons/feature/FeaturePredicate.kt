package net.skymoe.enchaddons.feature

import net.minecraft.client.gui.inventory.GuiChest
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.CancelableEvent
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.scope.longreturn
import net.skymoe.enchaddons.util.trimStyle

inline fun ensure(
    frame: Int = 0,
    predicate: () -> Boolean,
) {
    if (!predicate()) longreturn(frame) {}
}

fun Feature<*>.ensureEnabled(frame: Int = 0) = ensure(frame) { config.enabled }

inline fun <T : FeatureConfig> Feature<T>.ensureEnabled(
    frame: Int = 0,
    option: T.() -> Boolean,
) = ensure(frame) { config.enabled && option(config) }

fun ensureNotCanceled(
    event: CancelableEvent,
    frame: Int = 0,
) = ensure(frame) { !event.canceled }

fun ensureInWorld(frame: Int = 0) = ensure(frame) { MC.theWorld !== null }

fun ensureSkyBlock(frame: Int = 0) {
    ensure(frame) {
        MC.theWorld !== null &&
            EA.api.hypixelLocation
                ?.serverType
                ?.name == "SkyBlock"
    }
}

fun ensureSkyBlockMode(
    mode: String,
    frame: Int = 0,
) {
    ensure(frame) {
        MC.theWorld !== null && (EA.api.hypixelLocation?.run {
            serverType?.name == "SkyBlock" && this.mode == mode
        } ?: false)
    }
}

fun ensureSkyBlockModes(
    modes: Set<String>,
    frame: Int = 0,
) {
    ensure(frame) {
        MC.theWorld !== null && (EA.api.hypixelLocation?.run {
            serverType?.name == "SkyBlock" && mode in modes
        } ?: false)
    }
}

fun ensureWindowTitle(
    chest: GuiChest,
    title: String,
    frame: Int = 0,
) {
    ensure(frame) {
        EA.api.get_GuiChest_lowerChestInventory(chest)
            .name.trimStyle == title
    }
}

val SKYBLOCK_MINING_ISLANDS =
    setOf(
        "mining_1",
        "mining_2",
        "mining_3",
        "crystal_hollows",
        "mineshaft",
        "combat_3",
        "crimson_isle",
    )