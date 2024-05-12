package de.rasmusantons.playertracker.server.mixin;

import de.rasmusantons.playertracker.Utils;
import de.rasmusantons.playertracker.server.extension.ServerPlayerExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static net.minecraft.core.component.DataComponents.LODESTONE_TRACKER;

@Mixin(CompassItem.class)
public class CompassItemMixin {
    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void onInventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl, CallbackInfo ci) {
        if (entity instanceof ServerPlayer serverPlayer) {
            if (!Utils.isPlayerTracker(itemStack))
                return;
            ServerPlayer nowTracking = ((ServerPlayerExtension) serverPlayer).playertracker$getTrackedPlayer();
            if (nowTracking == null)
                return;
            BlockPos blockPos = nowTracking.getOnPos();
            LodestoneTracker lodestoneTracker = new LodestoneTracker(Optional.of(GlobalPos.of(nowTracking.level().dimension(), blockPos)), false);
            itemStack.set(LODESTONE_TRACKER, lodestoneTracker);
        }
    }
}
