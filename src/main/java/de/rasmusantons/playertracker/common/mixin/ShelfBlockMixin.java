package de.rasmusantons.playertracker.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.rasmusantons.playertracker.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ShelfBlock.class)
public class ShelfBlockMixin {
    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ShelfBlock;swapSingleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/ShelfBlockEntity;ILnet/minecraft/world/entity/player/Inventory;)Z"), cancellable = true)
    private static void preventSingleItemSwap(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (Utils.isPlayerTracker(player.getInventory().getSelectedItem())) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "swapHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getContainerSize()I"), cancellable = true)
    private static void preventHotbarSwap(Level level, BlockPos pos, Inventory inventory, CallbackInfoReturnable<Boolean> cir, @Local(name = "connectedBlocks") List<BlockPos> connectedBlocks, @Local(name = "shelfPart")ShelfBlockEntity shelfPart) {
        for (int inventorySlot = 8; inventorySlot > 8 - connectedBlocks.size() * shelfPart.getContainerSize(); --inventorySlot) {
            if (Utils.isPlayerTracker(inventory.getItem(inventorySlot))) {
                inventory.player.sendOverlayMessage(Utils.addFallback(Component.translatable("playertracker.action.prevent_shelf_swap")).withStyle(ChatFormatting.RED));
                cir.setReturnValue(false);
            }
        }
    }
}
