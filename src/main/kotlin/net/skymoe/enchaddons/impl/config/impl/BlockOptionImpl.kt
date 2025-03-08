package net.skymoe.enchaddons.impl.config.impl

import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Text
import net.skymoe.enchaddons.feature.config.BlockOption

class BlockOptionImpl : BlockOption {
    @Text(
        name = "Block ID",
        size = 1,
    )
    var idOption = "minecraft:air"

    @Number(
        name = "Block Metadata",
        min = 0.0F,
        max = 65535.0F,
        step = 1,
        size = 1,
    )
    var metaOption = 0

    override val id by ::idOption
    override val meta by ::metaOption
}
