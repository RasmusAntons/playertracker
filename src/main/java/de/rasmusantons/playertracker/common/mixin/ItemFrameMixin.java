package de.rasmusantons.playertracker.common.mixin;

import de.rasmusantons.playertracker.Utils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public class ItemFrameMixin {
    @Inject(method = "interact", at = @At(value = "HEAD"), cancellable = true)
    private void onInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (Utils.isPlayerTracker(itemStack))
            cir.setReturnValue(InteractionResult.PASS);
    }
}
