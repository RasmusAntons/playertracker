package de.rasmusantons.playertracker.network;

import de.rasmusantons.playertracker.network.c2s.TrackPlayerPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PlayerTrackerNetworking {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(TrackPlayerPacket.TYPE, TrackPlayerPacket.CODEC);
    }
}
