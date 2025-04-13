package de.rasmusantons.playertracker.common.mixin;

import de.rasmusantons.playertracker.Utils;
import de.rasmusantons.playertracker.server.extension.ServerPlayerExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static net.minecraft.core.component.DataComponents.LODESTONE_TRACKER;

@Mixin(CompassItem.class)
public class CompassItemMixin {
    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void onInventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl, CallbackInfo ci) {
        if (entity instanceof ServerPlayer serverPlayer) {
            if (!Utils.isPlayerTracker(itemStack))
                return;
            GlobalPos targetPos = null;
            ServerPlayer nowTracking = ((ServerPlayerExtension) serverPlayer).playertracker$getTrackedPlayer();
            if (nowTracking == null || nowTracking.hasDisconnected()) {
                if (nowTracking != null)
                    Utils.setTrackedPlayer(serverPlayer, null);
            } else {
                BlockPos blockPos = nowTracking.getOnPos();
                targetPos = GlobalPos.of(nowTracking.level().dimension(), blockPos);
            }
            if (targetPos == null) {
                for (var otherLevel : serverPlayer.getServer().levelKeys()) {
                    if (!otherLevel.equals(level.dimension())) {
                        targetPos = GlobalPos.of(otherLevel, new BlockPos(0, 0, 0));
                        break;
                    }
                }
            }
            LodestoneTracker lodestoneTracker = new LodestoneTracker(Optional.of(targetPos), false);
            itemStack.set(LODESTONE_TRACKER, lodestoneTracker);
        }
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void onUseOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (Utils.isPlayerTracker(context.getItemInHand())) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
