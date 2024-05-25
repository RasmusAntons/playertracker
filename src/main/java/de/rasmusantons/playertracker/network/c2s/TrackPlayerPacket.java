package de.rasmusantons.playertracker.network.c2s;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record TrackPlayerPacket(GameProfile player) implements CustomPacketPayload {
    public static final ResourceLocation IDENTIFIER = new ResourceLocation("playertracker", "track_player");
    public static final Type<TrackPlayerPacket> TYPE = new Type<>(IDENTIFIER);
    public static final StreamCodec<ByteBuf, TrackPlayerPacket> CODEC = ByteBufCodecs.GAME_PROFILE.map(
            TrackPlayerPacket::new,
            p -> p.player
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public GameProfile getPlayer() {
        return this.player;
    }
}
