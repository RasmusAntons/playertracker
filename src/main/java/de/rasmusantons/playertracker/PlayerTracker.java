package de.rasmusantons.playertracker;

import de.rasmusantons.playertracker.network.PlayerTrackerNetworking;
import de.rasmusantons.playertracker.network.c2s.TrackPlayerPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;

public class PlayerTracker implements ModInitializer {
    public static final GameRules.Key<GameRules.BooleanValue> GIVE_PLAYER_TRACKER =
            GameRuleRegistry.register("givePlayerTracker",
                    GameRules.Category.PLAYER,
                    GameRuleFactory.createBooleanRule(false)
            );

    public static String MOD_ID = "playertracker";

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        PlayerTrackerNetworking.init();
        ServerPlayNetworking.registerGlobalReceiver(TrackPlayerPacket.TYPE, (payload, context) -> {
            MinecraftServer server = context.player().getServer();
            if (server == null)
                return;
            ServerPlayer targetPlayer = server.getPlayerList().getPlayer(payload.getPlayer().getId());
            if (targetPlayer == null)
                return;
            Utils.setTrackedPlayer(context.player(), targetPlayer);
        });
    }
}
