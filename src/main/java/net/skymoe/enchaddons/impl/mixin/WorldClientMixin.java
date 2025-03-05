package net.skymoe.enchaddons.impl.mixin;

import net.minecraft.client.multiplayer.WorldClient;
import net.skymoe.enchaddons.impl.mixincallback.WorldClientMixinCallbackKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public class WorldClientMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        WorldClientMixinCallbackKt.onWorldLoadPost((WorldClient) (Object) this);
    }
}
