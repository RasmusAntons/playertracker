package de.rasmusantons.playertracker.server.extension;

import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerExtension {
    ServerPlayer playertracker$getTrackedPlayer();
    void playertracker$setTrackedPlayer(ServerPlayer trackedPlayer);
}
