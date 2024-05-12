package de.rasmusantons.playertracker.server.mixin;

import de.rasmusantons.playertracker.Utils;
import de.rasmusantons.playertracker.server.extension.ServerPlayerExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"))
    private void onUse(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack item = player.getItemInHand(interactionHand);
        if (player instanceof ServerPlayer serverPlayer && Utils.isPlayerTracker(item)) {
            ServerPlayer currentlyTracking = ((ServerPlayerExtension) serverPlayer).playertracker$getTrackedPlayer();
            List<ServerPlayer> players = new ArrayList<>(Objects.requireNonNull(level.getServer()).getPlayerList().getPlayers());
            players.remove(player);
            Component message;
            if (!players.isEmpty()) {
                int currentlyTrackingIdx = players.indexOf(currentlyTracking);
                int nowTrackingIdx = (currentlyTrackingIdx + 1) % players.size();
                ServerPlayer nowTracking = players.get(nowTrackingIdx);
                ((ServerPlayerExtension) serverPlayer).playertracker$setTrackedPlayer(nowTracking);
                String nowTrackingName = Objects.requireNonNull(nowTracking.getDisplayName()).getString();
                message = Component.literal(String.format("now tracking %s", nowTrackingName)).withStyle(ChatFormatting.GOLD);
            } else {
                message = Component.literal("no player to track").withStyle(ChatFormatting.GOLD);
            }
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
        }
    }
}
