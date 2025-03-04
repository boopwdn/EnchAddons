package net.skymoe.enchaddons.feature.dynamicspot

import net.minecraft.client.Minecraft
import net.skymoe.enchaddons.api.Template
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.featureInfo

val DYNAMIC_SPOT_INFO = featureInfo<DynamicSpotConfig>("dynamic_spot", "Dynamic Spot")

object DynamicSpot : FeatureBase<DynamicSpotConfig>(DYNAMIC_SPOT_INFO) {
    fun setPlaceholders(template: Template) {
        template["fps"] = Minecraft.getDebugFPS()
    }

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
        }
    }
}
