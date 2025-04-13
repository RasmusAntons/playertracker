package de.rasmusantons.playertracker;

import de.rasmusantons.playertracker.server.extension.ServerPlayerExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
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
        newCompass.set(LORE, new ItemLore(List.of(Utils.addFallback(
                Component.translatable("playertracker.lore.not_tracking")
        ).withStyle(ChatFormatting.GOLD))));
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("playertracker", true);
        newCompass.set(CUSTOM_DATA, CustomData.of(tag));
        LodestoneTracker lodestoneTracker = new LodestoneTracker(
                Optional.of(getDefaultTarget(level)),
                false
        );
        newCompass.set(LODESTONE_TRACKER, lodestoneTracker);
        return newCompass;
    }

    public static void showTrackingActionBarText(ServerPlayer player, ServerPlayer trackedPlayer) {
        Component message;
        if (trackedPlayer != null) {
            String nowTrackingName = Objects.requireNonNull(trackedPlayer.getDisplayName()).getString();
            message = Utils.addFallback(Component.translatable("playertracker.action.now_tracking", nowTrackingName))
                    .withStyle(ChatFormatting.GOLD);
        } else {
            message = Utils.addFallback(Component.translatable("playertracker.action.not_tracking"))
                    .withStyle(ChatFormatting.GOLD);
        }
        player.connection.send(new ClientboundSetActionBarTextPacket(message));
    }

    public static void setTrackedPlayer(ServerPlayer player, ServerPlayer trackedPlayer) {
        ((ServerPlayerExtension) player).playertracker$setTrackedPlayer(trackedPlayer);
        Component lore;
        if (trackedPlayer != null) {
            String nowTrackingName = Objects.requireNonNull(trackedPlayer.getDisplayName()).getString();
            lore = Utils.addFallback(Component.translatable("playertracker.lore.tracking", nowTrackingName))
                    .withStyle(ChatFormatting.GOLD);
        } else {
            lore = Utils.addFallback(Component.translatable("playertracker.lore.not_tracking"))
                    .withStyle(ChatFormatting.GOLD);
        }
        showTrackingActionBarText(player, trackedPlayer);
        player.getInventory().items.stream().filter(Utils::isPlayerTracker).forEach(playerTracker ->
                playerTracker.set(LORE, new ItemLore(List.of(lore)))
        );
    }

    public static GlobalPos getDefaultTarget(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            for (var otherLevel : serverLevel.getServer().levelKeys()) {
                if (!otherLevel.equals(level.dimension())) {
                    return GlobalPos.of(otherLevel, new BlockPos(0, 0, 0));
                }
            }
        }
        return GlobalPos.of(level.dimension(), new BlockPos(0, 0, 0));
    }

    public static MutableComponent addFallback(MutableComponent component) {
        if (component.getContents() instanceof TranslatableContents translatable) {
            Object[] args = translatable.getArgs();
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof MutableComponent subComponent) {
                        args[i] = addFallback(subComponent);
                    }
                }
            }
            final String fallbackText = Language.getInstance().getOrDefault(translatable.getKey(), null);
            return Component.translatableWithFallback(translatable.getKey(), fallbackText, args)
                    .withStyle(component.getStyle());
        }
        return component;
    }
}
