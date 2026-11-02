package net.pcal.splitscreen.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import static net.pcal.splitscreen.common.Mod.mod;

public class SplitscreenLayoutScreen extends Screen {

    private final Screen parent;
    private static final int PREVIEW_BUTTON_WIDTH = 120;
    private static final int PREVIEW_BUTTON_HEIGHT = 100;
    private static final int BUTTON_SPACING = 16;
    private static final int SMALL_BUTTON_WIDTH = 200;
    private static final int SMALL_BUTTON_HEIGHT = 20;
    private static final int COLOR_ZONE_HOVER = 0x1A44CC44;
    private static final int COLOR_ZONE_HOVER_DISABLED = 0x0A222222;

    private LayoutPreviewButton horizontalBtn;
    private LayoutPreviewButton verticalBtn;
    private LayoutPreviewButton quadBtn;

    private int[] zoneStarts = new int[3];
    private int[] zoneEnds = new int[3];
    private int hoveredZone = -1;
    private int prevHoveredZone = -1;

    public SplitscreenLayoutScreen(Screen parent) {
        super(Component.literal("Splitscreen Layout"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int totalBtnWidth = PREVIEW_BUTTON_WIDTH * 3 + BUTTON_SPACING * 2;
        int startX = centerX - totalBtnWidth / 2;
        int previewY = this.height / 2 - PREVIEW_BUTTON_HEIGHT / 2;

        zoneStarts[0] = 0;
        zoneEnds[0] = startX + PREVIEW_BUTTON_WIDTH / 2;
        zoneStarts[1] = zoneEnds[0];
        zoneEnds[1] = startX + PREVIEW_BUTTON_WIDTH + BUTTON_SPACING + PREVIEW_BUTTON_WIDTH / 2;
        zoneStarts[2] = zoneEnds[1];
        zoneEnds[2] = this.width;

        horizontalBtn = new LayoutPreviewButton(
                startX, previewY, PREVIEW_BUTTON_WIDTH, PREVIEW_BUTTON_HEIGHT,
                Component.literal("Horizontal"),
                SplitscreenLayout.HORIZONTAL,
                this.font,
                () -> selectLayout(SplitscreenLayout.HORIZONTAL)
        );

        verticalBtn = new LayoutPreviewButton(
                startX + PREVIEW_BUTTON_WIDTH + BUTTON_SPACING, previewY,
                PREVIEW_BUTTON_WIDTH, PREVIEW_BUTTON_HEIGHT,
                Component.literal("Vertical"),
                SplitscreenLayout.VERTICAL,
                this.font,
                () -> selectLayout(SplitscreenLayout.VERTICAL)
        );

        quadBtn = new LayoutPreviewButton(
                startX + (PREVIEW_BUTTON_WIDTH + BUTTON_SPACING) * 2, previewY,
                PREVIEW_BUTTON_WIDTH, PREVIEW_BUTTON_HEIGHT,
                Component.literal("Quad/Triple"),
                SplitscreenLayout.QUAD,
                this.font,
                () -> selectLayout(SplitscreenLayout.QUAD)
        );

        this.addRenderableWidget(horizontalBtn);
        this.addRenderableWidget(verticalBtn);
        this.addRenderableWidget(quadBtn);

        int cancelY = previewY + PREVIEW_BUTTON_HEIGHT + 30;
        this.addRenderableWidget(Button.builder(
                Component.literal("Cancel"),
                button -> this.minecraft.gui.setScreen(parent)
        ).bounds(centerX - SMALL_BUTTON_WIDTH / 2, cancelY, SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT).build());

        updateButtonStates();
    }

    private void updateButtonStates() {
        int count = mod().getInstanceCount();
        horizontalBtn.active = count == 2;
        verticalBtn.active = count == 2;
        quadBtn.active = count >= 3;
    }

    private int getZone(int mouseX) {
        for (int i = 0; i < 3; i++) {
            if (mouseX >= zoneStarts[i] && mouseX < zoneEnds[i]) return i;
        }
        return -1;
    }

    private boolean isZoneEnabled(int zone) {
        return switch (zone) {
            case 0 -> horizontalBtn.active;
            case 1 -> verticalBtn.active;
            case 2 -> quadBtn.active;
            default -> false;
        };
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        updateButtonStates();

        hoveredZone = getZone(mouseX);
        if (hoveredZone != prevHoveredZone) {
            if (hoveredZone >= 0 && isZoneEnabled(hoveredZone)) {
                Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.2F)
                );
            }
            prevHoveredZone = hoveredZone;
        }

        graphics.fill(0, 0, this.width, this.height, 0xCC000000);

        if (hoveredZone >= 0) {
            int c = isZoneEnabled(hoveredZone) ? COLOR_ZONE_HOVER : COLOR_ZONE_HOVER_DISABLED;
            graphics.fill(zoneStarts[hoveredZone], 50, zoneEnds[hoveredZone], this.height - 50, c);
        }

        Component title = Component.literal("Splitscreen Layout");
        int titleX = this.width / 2 - this.font.width(title) / 2;
        graphics.text(this.font, title, titleX, 20, 0xFFFFFFFF, true);

        int count = mod().getInstanceCount();
        Component subtitle;
        if (count <= 1) {
            subtitle = Component.literal("Start another instance to enable split-screen");
        } else if (count == 2) {
            subtitle = Component.literal("2 instances detected - select a 2-player layout");
        } else {
            subtitle = Component.literal(count + " instances detected - quad/triple split available");
        }
        int subtitleX = this.width / 2 - this.font.width(subtitle) / 2;
        graphics.text(this.font, subtitle, subtitleX, 35, 0xFFAAAAAA, true);

        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    private void selectLayout(SplitscreenLayout layout) {
        mod().applyLayout(layout);
        this.minecraft.gui.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
