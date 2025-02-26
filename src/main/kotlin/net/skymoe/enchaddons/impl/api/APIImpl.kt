package net.skymoe.enchaddons.impl.api

import net.minecraft.client.gui.GuiScreen
import net.minecraft.inventory.IInventory
import net.skymoe.enchaddons.api.API
import net.skymoe.enchaddons.api.HypixelLocation
import net.skymoe.enchaddons.getLogger
import net.skymoe.enchaddons.impl.mixin.AccessorGuiChest
import net.skymoe.enchaddons.impl.mixin.AccessorGuiScreen

private val logger = getLogger("API")

class APIImpl : API {
    override var hypixelLocation: HypixelLocation? = null
    override val templateProvider = ::TemplateImpl

    override fun get_GuiChest_lowerChestInventory(instance: GuiScreen): IInventory {
        return (instance as AccessorGuiChest).getLowerChestInventory()
    }

    override fun call_GuiScreen_keyTyped(instance: GuiScreen, typedChar: Char, keyCode: Int) {
        (instance as AccessorGuiScreen).keyTyped(typedChar, keyCode)
    }
}