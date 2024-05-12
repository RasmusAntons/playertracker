package de.rasmusantons.playertracker.server.mixin;

import de.rasmusantons.playertracker.Utils;
import de.rasmusantons.playertracker.common.mixin.PlayerMixin;
import de.rasmusantons.playertracker.server.extension.ServerPlayerExtension;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

import static de.rasmusantons.playertracker.PlayerTracker.GIVE_PLAYER_TRACKER;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin implements ServerPlayerExtension {
    @Unique
    public ServerPlayer playertracker$trackedPlayer = null;

    @Unique
    public ServerPlayer playertracker$getTrackedPlayer() {
        return this.playertracker$trackedPlayer;
    }

    @Unique
    public void playertracker$setTrackedPlayer(ServerPlayer trackedPlayer) {
        this.playertracker$trackedPlayer = trackedPlayer;
    }

    @Inject(method = "doTick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (this.level().getGameRules().getBoolean(GIVE_PLAYER_TRACKER)) {
            int nPlayerTrackers = Stream.concat(
                    this.getInventory().items.stream(),
                    Stream.of(this.getItemBySlot(EquipmentSlot.OFFHAND), this.containerMenu.getCarried())
            ).filter(Utils::isPlayerTracker).mapToInt(ItemStack::getCount).sum();
            if (nPlayerTrackers > 1) {
                this.getInventory().clearOrCountMatchingItems(Utils::isPlayerTracker, nPlayerTrackers - 1, this.getInventory());
            } else if (nPlayerTrackers == 0) {
                this.addItem(Utils.createPlayerTracker());
            }
        }
    }
}
