package net.skymoe.enchaddons.impl.nanovg.widget

import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.util.math.Vec2D

class ListWidget(
    private val list: MutableList<Widget<*>> = mutableListOf(),
) : Widget<ListWidget> {
    constructor(vararg widgets: Widget<*>) : this(widgets.toMutableList())

    override fun draw(context: NanoVGUIContext) {
        list.forEach { it.draw(context) }
    }

    override fun alphaScale(alpha: Double) = ListWidget(list.map { it.alphaScale(alpha) }.toMutableList())

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ) = ListWidget(list.map { it.scale(scale, origin) }.toMutableList())
}
