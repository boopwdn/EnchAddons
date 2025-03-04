package net.skymoe.enchaddons.impl.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.skymoe.enchaddons.impl.mixincallback.NetHandlerPlayClientCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class NetHandlerPlayClientMixin {
    @Shadow private Minecraft gameController;

    @Inject(method = "handleChat", at = @At("TAIL"))
    public void processPacket(S02PacketChat packetIn, CallbackInfo ci) {
//        if (!gameController.isCallingFromMinecraftThread()) return;
        NetHandlerPlayClientCallback.INSTANCE.onS02PacketChatPost(packetIn);
    }

    @Inject(method = "handleConfirmTransaction", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift = At.Shift.AFTER))
    public void processPacket(S32PacketConfirmTransaction packetIn, CallbackInfo ci) {
//        if (!gameController.isCallingFromMinecraftThread()) return;
        NetHandlerPlayClientCallback.INSTANCE.onS32PacketConfirmTransactionPre(packetIn);
    }
}
