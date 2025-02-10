package net.skymoe.enchaddons.command

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import net.skymoe.enchaddons.EnchAddons
import net.skymoe.enchaddons.EnchAddonsContants
import net.skymoe.enchaddons.config.TestConfig

@Command(value = EnchAddonsContants.MODID, description = "Access the " + EnchAddonsContants.NAME + " GUI.")
object ExampleCommand {
    @Main
    private fun handle() {
        TestConfig.openGui()
    }
}