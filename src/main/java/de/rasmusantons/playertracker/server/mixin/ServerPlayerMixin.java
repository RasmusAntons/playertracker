package de.rasmusantons.playertracker.server.mixin;

import de.rasmusantons.playertracker.Utils;
import de.rasmusantons.playertracker.common.mixin.PlayerMixin;
import de.rasmusantons.playertracker.server.extension.ServerPlayerExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.stream.Stream;

import static de.rasmusantons.playertracker.PlayerTracker.GIVE_PLAYER_TRACKER;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin implements ServerPlayerExtension {
    @Shadow @Final public MinecraftServer server;
    @Unique
    public UUID playertracker$trackedPlayer = null;

    @Unique
    public ServerPlayer playertracker$getTrackedPlayer() {
        return this.server.getPlayerList().getPlayer(this.playertracker$trackedPlayer);
    }

    @Unique
    public void playertracker$setTrackedPlayer(ServerPlayer trackedPlayer) {
        if (trackedPlayer != null) {
            this.playertracker$trackedPlayer = trackedPlayer.getUUID();
        } else {
            this.playertracker$trackedPlayer = null;
        }
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void onDrop(ItemStack droppedItem, boolean dropAround, boolean includeThrowerName, CallbackInfoReturnable<ItemEntity> cir) {
        if (Utils.isPlayerTracker(droppedItem))
            cir.setReturnValue(null);
    }

    @Inject(method = "doTick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(GIVE_PLAYER_TRACKER)) {
            int nPlayerTrackers = Stream.concat(
                    this.getInventory().items.stream(),
                    Stream.of(this.getItemBySlot(EquipmentSlot.OFFHAND), this.containerMenu.getCarried())
            ).filter(Utils::isPlayerTracker).mapToInt(ItemStack::getCount).sum();
            if (nPlayerTrackers > 1) {
                this.getInventory().clearOrCountMatchingItems(Utils::isPlayerTracker, nPlayerTrackers - 1, this.getInventory());
            } else if (nPlayerTrackers == 0) {
                this.addItem(Utils.createPlayerTracker(this.level()));
            }
        }
    }
}
