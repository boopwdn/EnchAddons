package net.skymoe.enchaddons

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import net.skymoe.enchaddons.command.ExampleCommand
import net.skymoe.enchaddons.config.TestConfig

object EnchAddons {
    // Register the config and commands.
    fun initialize() {
        TestConfig.initialize()
        CommandManager.INSTANCE.registerCommand(ExampleCommand)
    }
}