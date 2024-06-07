package de.rasmusantons.playertracker.server.mixin;

import de.rasmusantons.playertracker.Utils;
import de.rasmusantons.playertracker.server.extension.ServerPlayerExtension;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @Inject(method = "handleSetCarriedItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;resetLastActionTime()V"))
    private void onSetCarriedItem(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
        if (Utils.isPlayerTracker(this.player.getMainHandItem())) {
            ServerPlayer trackedPlayer = ((ServerPlayerExtension) this.player).playertracker$getTrackedPlayer();
            Utils.showTrackingActionBarText(this.player, trackedPlayer);
        }
    }
}
