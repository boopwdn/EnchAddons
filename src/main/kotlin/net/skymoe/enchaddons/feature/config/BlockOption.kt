package net.skymoe.enchaddons.feature.config

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.skymoe.enchaddons.util.scope.noexcept

interface BlockOption {
    val id: String
    val meta: Int
}

val BlockOption.blockState: IBlockState
    get() = noexcept { Block.getBlockFromName(id).getStateFromMeta(meta) } ?: Blocks.air.defaultState
