package net.skymoe.enchaddons.hud

import cc.polyfrost.oneconfig.hud.SingleTextHud

object TestHud : SingleTextHud("Test", true) {
    public override fun getText(example: Boolean): String {
        return "I'm an example HUD"
    }
}
