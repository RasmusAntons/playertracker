package de.rasmusantons.playertracker.server;

import de.rasmusantons.playertracker.Utils;
import de.rasmusantons.playertracker.network.c2s.TrackPlayerPacket;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PlayerTrackerServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
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
