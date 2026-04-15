package de.rasmusantons.playertracker;

import de.rasmusantons.playertracker.network.PlayerTrackerNetworking;
import de.rasmusantons.playertracker.network.c2s.TrackPlayerPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class PlayerTracker implements ModInitializer {

    public static String MOD_ID = "playertracker";

    public static final GameRule<Boolean> GIVE_PLAYER_TRACKER =
            GameRuleBuilder
                    .forBoolean(false)
                    .category(GameRuleCategory.PLAYER)
                    .buildAndRegister(id("give_player_tracker"));

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        PlayerTrackerNetworking.init();
        ServerPlayNetworking.registerGlobalReceiver(TrackPlayerPacket.TYPE, (payload, context) -> {
            System.out.println("received track player packet");
            ServerPlayer targetPlayer = context.server().getPlayerList().getPlayer(payload.getPlayer().id());
            if (targetPlayer == null)
                return;
            Utils.setTrackedPlayer(context.player(), targetPlayer);
        });
    }
}
