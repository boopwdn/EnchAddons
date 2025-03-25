package net.skymoe.enchaddons.impl.config.feature

import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.elements.SubConfig
import net.skymoe.enchaddons.feature.dungeon.fastdraft.FAST_DRAFT_INFO
import net.skymoe.enchaddons.feature.dungeon.fastdraft.FastDraftConfig
import net.skymoe.enchaddons.impl.config.ConfigImpl
import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.impl.config.adapter.Extract
import net.skymoe.enchaddons.impl.config.announcement.DynamicKeyBindingDependent
import net.skymoe.enchaddons.impl.config.announcement.DynamicSpotDependent

class FastDraftConfigImpl :
    ConfigImpl(FAST_DRAFT_INFO),
    FastDraftConfig {
    override val enabled get() =
        EnchAddonsConfig.enabled &&
            (EnchAddonsConfig.dungeonConfig as SubConfig).enabled &&
            (this as SubConfig).enabled

    @Transient
    @Extract
    val dynamicSpotDependent = DynamicSpotDependent()

    @Switch(
        name = "Enabled",
        size = 1,
        category = "General",
        subcategory = "Chat Hint",
    )
    override var chatHintEnabled = true

    @Transient
    @Extract
    val dynamicKeyBindingDependent = DynamicKeyBindingDependent()

    @Switch(
        name = "Enabled",
        size = 1,
        category = "General",
        subcategory = "Dynamic KeyBinding",
    )
    override var dynamicKeyBindingEnabled = true

    @Text(
        name = "Action text",
        size = 1,
        category = "General",
        subcategory = "Dynamic KeyBinding",
    )
    override var actionText: String = "Get a draft"
}
