package de.rasmusantons.playertracker.common.mixin;

import de.rasmusantons.playertracker.Utils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantOffer.class)
public class MerchantOfferMixin {
    @Inject(method = "satisfiedBy", at = @At("HEAD"), cancellable = true)
    private void onSatisfiedBy(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<Boolean> cir) {
        if (Utils.isPlayerTracker(itemStack) || Utils.isPlayerTracker(itemStack2)) {
            cir.setReturnValue(false);
        }
    }
}