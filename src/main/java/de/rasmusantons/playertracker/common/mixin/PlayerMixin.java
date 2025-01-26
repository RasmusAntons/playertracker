package de.rasmusantons.playertracker.common.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Player.class)
public abstract class PlayerMixin extends EntityMixin {
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);
    @Shadow public abstract Inventory getInventory();
    @Shadow public abstract boolean addItem(ItemStack stack);
    @Shadow public AbstractContainerMenu containerMenu;
}
