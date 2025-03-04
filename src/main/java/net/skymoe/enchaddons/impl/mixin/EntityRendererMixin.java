package net.skymoe.enchaddons.impl.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import net.skymoe.enchaddons.impl.mixincallback.EntityRendererCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Inject(method = "updateCameraAndRender", at = @At("HEAD"))
    private void updateCameraAndRenderPre(float partialTicks, long nanoTime, CallbackInfo ci) {
        EntityRendererCallback.General.INSTANCE.updateCameraAndRenderPre(partialTicks);
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V", shift = At.Shift.AFTER))
    private void updateCameraAndRenderRenderHUD(float partialTicks, long nanoTime, CallbackInfo ci) {
        EntityRendererCallback.General.INSTANCE.updateCameraAndRenderRenderHUD();
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;", ordinal = 1))
    private void updateCameraAndRenderProxyScreenAtCondition(float partialTicks, long nanoTime, CallbackInfo ci) {
        EntityRendererCallback.General.INSTANCE.updateCameraAndRenderProxyScreenAtCondition();
    }
}
