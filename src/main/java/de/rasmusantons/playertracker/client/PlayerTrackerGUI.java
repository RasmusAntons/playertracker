package de.rasmusantons.playertracker.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PlayerTrackerGUI extends Screen {
    final static int CONTAINER_WIDTH = 176;
    final static int CONTAINER_HEIGHT = 96;
    final static int TITLE_LEFT = 8;
    final static int TITLE_TOP = 6;
    final static int SLOTS_LEFT = 8;
    final static int SLOTS_TOP = 18;
    final static int SLOTS_SIZE = 18;

    private PlayerInfo hoveredPlayer = null;
    private List<Component> tooltip = null;
    private final Consumer<PlayerInfo> onSelect;

    protected PlayerTrackerGUI(Consumer<PlayerInfo> onSelect) {
        super(Component.translatable("playertracker.gui.title"));
        this.onSelect = onSelect;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        float scale = 1f;

        int x = (int) ((this.width / scale - CONTAINER_WIDTH) / 2);
        int y = (int) ((this.height / scale - CONTAINER_HEIGHT) / 2);
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1);
        graphics.pose().translate(x, y, 0);
        this.renderContainer(graphics, (int) (mouseX / scale - x), (int) (mouseY / scale - y));
        graphics.pose().popPose();
        if (this.tooltip != null) {
            graphics.renderTooltip(this.minecraft.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    protected void renderContainer(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.blitSprite(new ResourceLocation("playertracker", "container"),
                0, 0, 256, 256);
        graphics.drawString(this.minecraft.font, Component.translatable("playertracker.gui.title"),
                TITLE_LEFT, TITLE_TOP, 0x404040, false);
        List<PlayerInfo> onlinePlayers = this.minecraft.getConnection().getOnlinePlayers().stream().filter(
                p -> !p.getProfile().getId().equals(this.minecraft.player.getUUID())
        ).toList();
        boolean hoveringAny = false;
        for (int i = 0; i < onlinePlayers.size(); i++) {
            PlayerInfo playerInfo = onlinePlayers.get(i);
            graphics.pose().pushPose();
            int row = i / 9;
            int col = i % 9;
            graphics.pose().translate(SLOTS_LEFT + SLOTS_SIZE * col, SLOTS_TOP + SLOTS_SIZE * row, 10);
            this.renderPlayer(graphics, playerInfo);
            if (isHovering(row, col, mouseX, mouseY)) {
                hoveringAny = true;
                graphics.fill(RenderType.guiOverlay(), 0, 0, 16, 16, 15, 0x80ffffff);
                if (!playerInfo.equals(this.hoveredPlayer)) {
                    this.hoveredPlayer = playerInfo;
                    this.tooltip = createTooltip(playerInfo);
                }
            }
            graphics.pose().popPose();
        }
        if (!hoveringAny) {
            this.hoveredPlayer = null;
            this.tooltip = null;
        }
    }

    protected void renderPlayer(GuiGraphics graphics, PlayerInfo playerInfo) {
        ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);
        playerHead.set(DataComponents.PROFILE, new ResolvableProfile(playerInfo.getProfile()));
        graphics.renderFakeItem(playerHead, 0, 0);
    }

    protected List<Component> createTooltip(PlayerInfo playerInfo) {
        return List.of(
                Component.literal(playerInfo.getProfile().getName()),
                Component.translatable("playertracker.gui.click_to_track").withStyle(ChatFormatting.GRAY)
        );
    }

    protected boolean isHovering(int row, int col, float mouseX, float mouseY) {
        return mouseX >= SLOTS_LEFT + SLOTS_SIZE * col - 1
                && mouseX < SLOTS_LEFT + SLOTS_SIZE * (col + 1) - 1
                && mouseY >= SLOTS_TOP + SLOTS_SIZE * row - 1
                && mouseY < SLOTS_TOP + SLOTS_SIZE * (row + 1) - 1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.hoveredPlayer != null) {
            this.onClose();
            this.onSelect.accept(this.hoveredPlayer);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
