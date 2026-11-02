package net.pcal.splitscreen.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class LayoutPreviewButton extends AbstractButton {

    private static final int COLOR_BG_DARK = 0x99000000;
    private static final int COLOR_BG_HOVER = 0x991A331A;
    private static final int COLOR_WHITE_15 = 0x26FFFFFF;
    private static final int COLOR_BORDER = 0x80FFFFFF;
    private static final int COLOR_BORDER_HOVER = 0x8044CC44;
    private static final int COLOR_BORDER_INACTIVE = 0x40404040;

    private final SplitscreenLayout layout;
    private final Font font;
    private final Runnable onPressCallback;
    private boolean wasHovered;

    public LayoutPreviewButton(int x, int y, int width, int height,
                               Component label, SplitscreenLayout layout,
                               Font font, Runnable onPress) {
        super(x, y, width, height, label);
        this.layout = layout;
        this.font = font;
        this.onPressCallback = onPress;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F));
        if (this.onPressCallback != null) {
            this.onPressCallback.run();
        }
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();
        boolean hovered = isHovered();

        if (hovered && !wasHovered && active) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 1.2F));
        }
        wasHovered = hovered;

        int bgColor = (hovered && active) ? COLOR_BG_HOVER : COLOR_BG_DARK;
        int borderColor = !active ? COLOR_BORDER_INACTIVE : (hovered ? COLOR_BORDER_HOVER : COLOR_BORDER);

        graphics.fill(x, y, x + w, y + h, bgColor);

        drawOuterBorder(graphics, x, y, w, h, borderColor);

        int previewX = x + 8;
        int previewY = y + 8;
        int previewW = w - 16;
        int previewH = h - 32;

        drawLayoutPreview(graphics, previewX, previewY, previewW, previewH);

        int textColor = active ? 0xFFFFFFFF : 0xFF666666;
        int textX = x + w / 2 - this.font.width(getMessage()) / 2;
        int textY = y + h - 18;
        graphics.text(this.font, getMessage(), textX, textY, textColor, true);
    }

    private void drawOuterBorder(GuiGraphicsExtractor graphics, int x, int y, int w, int h, int color) {
        graphics.fill(x, y, x + w, y + 2, color);
        graphics.fill(x, y + h - 2, x + w, y + h, color);
        graphics.fill(x, y, x + 2, y + h, color);
        graphics.fill(x + w - 2, y, x + w, y + h, color);
    }

    private void drawLayoutPreview(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        switch (layout) {
            case HORIZONTAL -> drawHorizontalSplit(graphics, x, y, w, h);
            case VERTICAL -> drawVerticalSplit(graphics, x, y, w, h);
            case QUAD -> drawQuadSplit(graphics, x, y, w, h);
            case FULLSCREEN -> drawFullscreen(graphics, x, y, w, h);
            case WINDOWED -> drawWindowed(graphics, x, y, w, h);
        }
    }

    private void drawHorizontalSplit(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        int gap = 2;
        int halfW = (w - gap) / 2;
        graphics.fill(x, y, x + halfW, y + h, COLOR_WHITE_15);
        drawBorder(graphics, x, y, halfW, h);
        graphics.fill(x + halfW + gap, y, x + w, y + h, COLOR_WHITE_15);
        drawBorder(graphics, x + halfW + gap, y, w - halfW - gap, h);
    }

    private void drawVerticalSplit(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        int gap = 2;
        int halfH = (h - gap) / 2;
        graphics.fill(x, y, x + w, y + halfH, COLOR_WHITE_15);
        drawBorder(graphics, x, y, w, halfH);
        graphics.fill(x, y + halfH + gap, x + w, y + h, COLOR_WHITE_15);
        drawBorder(graphics, x, y + halfH + gap, w, h - halfH - gap);
    }

    private void drawQuadSplit(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        int gap = 2;
        int halfW = (w - gap) / 2;
        int halfH = (h - gap) / 2;
        graphics.fill(x, y, x + halfW, y + halfH, COLOR_WHITE_15);
        drawBorder(graphics, x, y, halfW, halfH);
        graphics.fill(x + halfW + gap, y, x + w, y + halfH, COLOR_WHITE_15);
        drawBorder(graphics, x + halfW + gap, y, w - halfW - gap, halfH);
        graphics.fill(x, y + halfH + gap, x + halfW, y + h, COLOR_WHITE_15);
        drawBorder(graphics, x, y + halfH + gap, halfW, h - halfH - gap);
        graphics.fill(x + halfW + gap, y + halfH + gap, x + w, y + h, COLOR_WHITE_15);
        drawBorder(graphics, x + halfW + gap, y + halfH + gap, w - halfW - gap, h - halfH - gap);
    }

    private void drawFullscreen(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        graphics.fill(x, y, x + w, y + h, COLOR_WHITE_15);
        drawBorder(graphics, x, y, w, h);
    }

    private void drawWindowed(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        int inset = w / 6;
        graphics.fill(x + inset, y + inset, x + w - inset, y + h - inset, COLOR_WHITE_15);
        drawBorder(graphics, x + inset, y + inset, w - inset * 2, h - inset * 2);
    }

    private void drawBorder(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        graphics.fill(x, y, x + w, y + 1, COLOR_BORDER);
        graphics.fill(x, y + h - 1, x + w, y + h, COLOR_BORDER);
        graphics.fill(x, y, x + 1, y + h, COLOR_BORDER);
        graphics.fill(x + w - 1, y, x + w, y + h, COLOR_BORDER);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }
}
