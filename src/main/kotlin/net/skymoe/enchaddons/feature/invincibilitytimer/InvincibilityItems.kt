package net.skymoe.enchaddons.feature.invincibilitytimer

import net.skymoe.enchaddons.feature.invincibilitytimer.InvincibilityTimer.InvincibilityItem

val INVINCIBILITY_ITEMS: List<InvincibilityItem> =
    listOf(
        InvincibilityItem(
            name = "Spirit Mask",
            invincibilityTicks = { 60 },
            coolDown = 30 * 20,
            regex = Regex("^Second Wind Activated! Your Spirit Mask saved your life!$"),
        ),
        InvincibilityItem(
            name = "Bonzo's Mask",
            invincibilityTicks = { 60 },
            coolDown = 3 * 60 * 20,
            regex = Regex("^Your (?:. )?Bonzo's Mask saved your life!$"),
        ),
        InvincibilityItem(
            name = "Phoenix Pet",
            invincibilityTicks = { InvincibilityTimer.config.phoenixPetTicks },
            coolDown = 60 * 20,
            regex = Regex("^Your Phoenix Pet saved you from certain death!$"),
        ),
    )
