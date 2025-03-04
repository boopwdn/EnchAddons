package net.skymoe.enchaddons.impl.mixin;

import net.minecraft.client.Minecraft;
import net.skymoe.enchaddons.impl.mixincallback.MinecraftMixinCallbackKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "startGame", at = @At("RETURN"))
    private void onStartGamePost(CallbackInfo ci) {
        MinecraftMixinCallbackKt.startGamePost();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onRunTickPre(CallbackInfo ci) { MinecraftMixinCallbackKt.onRunTickPre(); }
}
