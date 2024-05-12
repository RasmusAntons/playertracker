package de.rasmusantons.playertracker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

import static net.minecraft.core.component.DataComponents.CUSTOM_DATA;
import static net.minecraft.core.component.DataComponents.ITEM_NAME;

public class Utils {
    public static boolean isPlayerTracker(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof CompassItem))
            return false;
        CustomData customData = itemStack.get(CUSTOM_DATA);
        if (customData == null)
            return false;
        return customData.copyTag().getBoolean("playertracker");
    }

    public static ItemStack createPlayerTracker() {
        ItemStack newCompass = new ItemStack(Items.COMPASS);
        Component itemName = Component.literal("Player Tracker");
        newCompass.set(ITEM_NAME, itemName);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("playertracker", true);
        newCompass.set(CUSTOM_DATA, CustomData.of(tag));
        return newCompass;
    }
}
