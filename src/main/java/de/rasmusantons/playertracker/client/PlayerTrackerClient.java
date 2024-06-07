package de.rasmusantons.playertracker.client;

import de.rasmusantons.playertracker.Utils;
import de.rasmusantons.playertracker.network.c2s.TrackPlayerPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class PlayerTrackerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyMapping keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.playertracker.gui",
                GLFW.GLFW_KEY_P,
                "category.playertracker"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.consumeClick()) {
                Minecraft minecraft = Minecraft.getInstance();
                var connection = minecraft.getConnection();
                if (connection == null || connection.getOnlinePlayers().size() <= 1) {
                    minecraft.gui.setOverlayMessage(
                            Utils.addFallback(Component.translatable("playertracker.action.no_player"))
                                    .withStyle(ChatFormatting.GOLD),
                            false
                    );
                    return;
                }
                client.setScreen(new PlayerTrackerGUI(
                        (PlayerInfo selectedPlayer) ->
                                ClientPlayNetworking.send(new TrackPlayerPacket(selectedPlayer.getProfile()))
                ));
            }
        });
    }
}
