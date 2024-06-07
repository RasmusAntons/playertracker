package de.rasmusantons.playertracker;

import de.rasmusantons.playertracker.server.extension.ServerPlayerExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;
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

    public static void setTrackedPlayer(ServerPlayer player, ServerPlayer trackedPlayer) {
        ((ServerPlayerExtension) player).playertracker$setTrackedPlayer(trackedPlayer);
        Component message, lore;
        if (trackedPlayer != null) {
            String nowTrackingName = Objects.requireNonNull(trackedPlayer.getDisplayName()).getString();
            message = Component.translatable("playertracker.action.now_tracking", nowTrackingName).withStyle(ChatFormatting.GOLD);
            lore = Component.translatable("playertracker.lore.tracking", nowTrackingName).withStyle(ChatFormatting.GOLD);
        } else {
            message = Component.translatable("playertracker.action.no_longer_tracking");
            lore = Component.translatable("playertracker.lore.not_tracking");
        }
        player.connection.send(new ClientboundSetActionBarTextPacket(message));
        player.getInventory().items.stream().filter(Utils::isPlayerTracker).forEach(playerTracker -> {
            playerTracker.set(LORE, new ItemLore(List.of(lore)));
        });
    }
}
