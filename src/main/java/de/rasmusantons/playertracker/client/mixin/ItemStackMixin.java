package de.rasmusantons.playertracker.client.mixin;

import de.rasmusantons.playertracker.Utils;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "matchesIgnoringComponents", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/component/PatchedDataComponentMap;keySet()Ljava/util/Set;"), cancellable = true)
    private static void matchesIgnoringComponents(ItemStack a, ItemStack b, Predicate<DataComponentType<?>> ignoredPredicate, CallbackInfoReturnable<Boolean> cir) {
        if (Utils.isPlayerTracker(a) && Utils.isPlayerTracker(b)) {
            cir.setReturnValue(true);
        }
    }
}
