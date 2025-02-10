package net.skymoe.enchaddons.mixin;

import net.minecraft.client.Minecraft;
import net.skymoe.enchaddons.EnchAddons;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * An example mixin using SpongePowered's Mixin library
 *
 * @see Inject
 * @see Mixin
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "startGame", at = @At(value = "RETURN"))
    private void onStartGame(CallbackInfo ci) {
        EnchAddons.INSTANCE.initialize();
    }
}
