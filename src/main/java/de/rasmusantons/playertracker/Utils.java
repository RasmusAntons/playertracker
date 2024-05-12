package de.rasmusantons.playertracker;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

import static net.minecraft.core.component.DataComponents.*;

public class Utils {
    public static boolean isPlayerTracker(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof CompassItem))
            return false;
        CustomData customData = itemStack.get(CUSTOM_DATA);
        if (customData == null)
            return false;
        return customData.copyTag().getBoolean("playertracker");
    }

    public static ItemStack createPlayerTracker(Level level) {
        ItemStack newCompass = new ItemStack(Items.COMPASS);
        Component itemName = Component.literal("Player Tracker");
        newCompass.set(ITEM_NAME, itemName);
        newCompass.set(LORE, new ItemLore(List.of(Component.translatable("playertracker.lore.not_tracking").withStyle(ChatFormatting.GOLD))));
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("playertracker", true);
        newCompass.set(CUSTOM_DATA, CustomData.of(tag));
        BlockPos blockPos = BlockPos.ZERO;
        LodestoneTracker lodestoneTracker = new LodestoneTracker(Optional.of(GlobalPos.of(level.dimension(), blockPos)), false);
        newCompass.set(LODESTONE_TRACKER, lodestoneTracker);
        return newCompass;
    }
}
