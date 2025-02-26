package net.skymoe.enchaddons.api

import net.minecraft.client.gui.GuiScreen
import net.minecraft.inventory.IInventory

@Suppress("ktlint:standard:function-naming")
interface API {
    val hypixelLocation: HypixelLocation?
    val templateProvider: TemplateProvider

    fun call_GuiScreen_keyTyped(
        instance: GuiScreen,
        typedChar: Char,
        keyCode: Int,
    )

    fun get_GuiChest_lowerChestInventory(instance: GuiScreen): IInventory
}

inline fun API.format(
    template: String,
    placeholder: Template.() -> Unit,
): String {
    return templateProvider(template).also(placeholder).format()
}