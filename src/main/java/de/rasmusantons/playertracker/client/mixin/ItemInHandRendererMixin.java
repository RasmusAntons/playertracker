package de.rasmusantons.playertracker.client.mixin;

import de.rasmusantons.playertracker.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow private ItemStack mainHandItem;
    @Shadow private ItemStack offHandItem;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (this.minecraft.player == null)
            return;
        ItemStack newMainStack = this.minecraft.player.getMainHandItem();
        if (this.mainHandItem.getItem() == newMainStack.getItem() && Utils.isPlayerTracker(newMainStack)) {
            this.mainHandItem = newMainStack;
        }
        ItemStack newOffStack = this.minecraft.player.getOffhandItem();
        if (this.offHandItem.getItem() == newOffStack.getItem() && Utils.isPlayerTracker(newOffStack)) {
            this.offHandItem = newOffStack;
        }
    }
}
