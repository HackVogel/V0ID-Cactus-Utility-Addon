package dev.hackvogel.v0id.screens;

import com.dwarslooper.cactus.client.systems.config.CactusSettings;
import dev.hackvogel.v0id.utils.ServerInfoManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.awt.*;

public class ExtendedServerWidgetDebugScreen extends Screen {
    private EditBox inputField;
    private String targetIp = "";

    public ExtendedServerWidgetDebugScreen(MutableComponent empty) {
        super(Component.literal("Server Debug"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        inputField = new EditBox(this.font, centerX - 100, 40, 200, 20, Component.literal("IP/Domain"));
        inputField.setMaxLength(255);
        inputField.setResponder(value -> targetIp = value);
        this.addRenderableWidget(inputField);

        this.addRenderableWidget(Button.builder(Component.literal("Lookup"), btn -> {
            ServerInfoManager.debugInfo(targetIp);
        }).bounds(centerX - 50, 70, 100, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Close"), btn -> {
            Minecraft.getInstance().setScreen(null);
        }).bounds(centerX - 50, this.height - 30, 100, 20).build());
    }

    private void drawInfoLine(GuiGraphics graphics, String label, String value, int x, int y, int labelColor, int valueColor, int maxWidth) {
        graphics.drawString(this.font, label, x, y, labelColor, true);
        graphics.drawString(this.font, this.font.plainSubstrByWidth(value, maxWidth), x, y + 10, valueColor, true);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        int maxWidth = 260;
        int boxWidth = Math.min(maxWidth, this.width / 3);
        if (boxWidth < 150) boxWidth = 150;

        int maxHeight = 300;
        int boxHeight = Math.min(maxHeight, this.height - 40);

        // Mittig unter dem Lookup Button (der ist bei y=70, height=20)
        int x = this.width / 2 - boxWidth / 2;
        int y = 100;

        graphics.fill(x, y, x + boxWidth, y + boxHeight, 0xCC101010);

        int borderColor = new Color(CactusSettings.get().accentColor.get().color(), true).darker().getRGB();

        graphics.fill(x, y, x + boxWidth, y + 1, borderColor);
        graphics.fill(x, y + boxHeight - 1, x + boxWidth, y + boxHeight, borderColor);
        graphics.fill(x, y, x + 1, y + boxHeight, borderColor);
        graphics.fill(x + boxWidth - 1, y, x + boxWidth, y + boxHeight, borderColor);

        int headerHeight = 20;
        graphics.fill(x, y, x + boxWidth, y + headerHeight, CactusSettings.get().accentColor.get().color());
        graphics.drawCenteredString(this.font, "Server Info", x + boxWidth / 2, y + 6, 0xFFFFFF);

        int textX = x + 10;
        int textY = y + headerHeight + 10;
        int labelColor = 0xFFAAAAAA;
        int valueColor = CactusSettings.get().accentColor.get().color();
        int lineHeight = 10;
        int spacing = 4;

        int maxY = y + boxHeight - 10;

        if (textY < maxY) {
            drawInfoLine(graphics, "IP:", ServerInfoManager.getIp(), textX, textY, labelColor, valueColor, boxWidth - 20);
            textY += lineHeight * 2 + spacing;
        }
        if (textY < maxY) {
            drawInfoLine(graphics, "Reverse:", ServerInfoManager.getReverse(), textX, textY, labelColor, valueColor, boxWidth - 20);
            textY += lineHeight * 2 + spacing;
        }
        if (textY < maxY) {
            graphics.drawString(this.font, "Name Server:", textX, textY, labelColor, true);
            textY += lineHeight;
            String[] nameServers = ServerInfoManager.getNameServer().split(", ");
            for (String ns : nameServers) {
                if (textY >= maxY) break;
                graphics.drawString(this.font, this.font.plainSubstrByWidth(ns.trim(), boxWidth - 20), textX, textY, valueColor, true);
                textY += lineHeight;
            }
            textY += spacing;
        }
        if (textY < maxY) {
            drawInfoLine(graphics, "ASN:", ServerInfoManager.getAsn(), textX, textY, labelColor, valueColor, boxWidth - 20);
            textY += lineHeight * 2 + spacing;
        }
        if (textY < maxY) {
            drawInfoLine(graphics, "Net Name:", ServerInfoManager.getNetName(), textX, textY, labelColor, valueColor, boxWidth - 20);
            textY += lineHeight * 2 + spacing;
        }
        if (textY < maxY) {
            drawInfoLine(graphics, "Net Description:", ServerInfoManager.getNetDesc(), textX, textY, labelColor, valueColor, boxWidth - 20);
            textY += lineHeight * 2 + spacing;
        }
        if (textY < maxY) {
            drawInfoLine(graphics, "Organisation Name:", ServerInfoManager.getOrg(), textX, textY, labelColor, valueColor, boxWidth - 20);
        }
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}