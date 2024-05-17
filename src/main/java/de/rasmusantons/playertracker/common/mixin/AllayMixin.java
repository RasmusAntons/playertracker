package de.rasmusantons.playertracker.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.rasmusantons.playertracker.Utils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Allay.class)
public class AllayMixin {
    @Inject(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
                    shift = At.Shift.BY, by = 2
            ),
            cancellable = true
    )
    private void onInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local ItemStack itemStack) {
        if (Utils.isPlayerTracker(itemStack))
            cir.setReturnValue(InteractionResult.PASS);
    }
}
