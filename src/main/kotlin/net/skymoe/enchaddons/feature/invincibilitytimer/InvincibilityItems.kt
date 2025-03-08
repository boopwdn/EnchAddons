package net.skymoe.enchaddons.feature.invincibilitytimer

import net.skymoe.enchaddons.feature.invincibilitytimer.InvincibilityTimer.InvincibilityItem
import net.skymoe.enchaddons.impl.cache.ResourceCacheImpl
import net.skymoe.enchaddons.impl.oneconfig.nvg

private fun getImageLoader(path: String): (Long) -> Int {
    var image: Int? = null
    return { vg ->
        (image ?: nvg.loadImageFromByteArray(vg, ResourceCacheImpl.get(path))).also { image = it }
    }
}

val INVINCIBILITY_ITEMS: List<InvincibilityItem> =
    listOf(
        InvincibilityItem(
            name = "Spirit Mask",
            invincibilityTicks = { 60 },
            coolDown = 30 * 20,
            regex = Regex("^Second Wind Activated! Your Spirit Mask saved your life!$"),
            image = getImageLoader("textures/items/spirit_mask.png"),
        ),
        InvincibilityItem(
            name = "Bonzo's Mask",
            invincibilityTicks = { 60 },
            coolDown = 3 * 60 * 20,
            regex = Regex("^Your (?:. )?Bonzo's Mask saved your life!$"),
            image = getImageLoader("textures/items/bonzo_mask.png"),
        ),
        InvincibilityItem(
            name = "Phoenix Pet",
            invincibilityTicks = { InvincibilityTimer.config.phoenixPetTicks },
            coolDown = 60 * 20,
            regex = Regex("^Your Phoenix Pet saved you from certain death!$"),
            image = getImageLoader("textures/items/phoenix_pet.png"),
        ),
    )
