package net.skymoe.enchaddons.impl.mixin;

import net.minecraft.client.main.Main;
import net.skymoe.enchaddons.impl.mixincallback.MainMixinCallbackKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public abstract class MainMixin {
    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void mainPre(String[] arguments, CallbackInfo ci) {
        MainMixinCallbackKt.mainPre();
    }
}
