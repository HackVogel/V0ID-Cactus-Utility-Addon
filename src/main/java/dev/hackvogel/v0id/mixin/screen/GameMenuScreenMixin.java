package dev.hackvogel.v0id.mixin.screen;

import com.dwarslooper.cactus.client.systems.config.CactusSettings;
import dev.hackvogel.v0id.utils.ServerInfoManager;
import dev.hackvogel.v0id.utils.V0IDUtility;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(PauseScreen.class)
public class GameMenuScreenMixin extends Screen {


    protected GameMenuScreenMixin(Component component) {
        super(component);
    }


    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (V0IDUtility.serverInfo) {
            ServerInfoManager.updateInfo();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (V0IDUtility.serverInfo) {
            int maxWidth = 260;
            int boxWidth = Math.min(maxWidth, this.width / 3);
            if (boxWidth < 150) boxWidth = 150;

            int maxHeight = 300;
            int boxHeight = Math.min(maxHeight, this.height - 40);

            int x = 10;
            int y = (this.height - boxHeight) / 2;

            guiGraphics.fill(x, y, x + boxWidth, y + boxHeight, 0xCC101010);

            int borderColor = new Color(CactusSettings.get().accentColor.get().color(), true).darker().getRGB();

            guiGraphics.fill(x, y, x + boxWidth, y + 1, borderColor);
            guiGraphics.fill(x, y + boxHeight - 1, x + boxWidth, y + boxHeight, borderColor);
            guiGraphics.fill(x, y, x + 1, y + boxHeight, borderColor);
            guiGraphics.fill(x + boxWidth - 1, y, x + boxWidth, y + boxHeight, borderColor);

            int headerHeight = 20;
            //guiGraphics.fill(x, y, x + boxWidth, y + headerHeight, 0x8855FFFF);
            guiGraphics.fill(x, y, x + boxWidth, y + headerHeight, CactusSettings.get().accentColor.get().color());
            guiGraphics.drawCenteredString(this.font, "Server Info", x + boxWidth / 2, y + 6, 0xFFFFFF);

            int textX = x + 10;
            int textY = y + headerHeight + 10;
            int labelColor = 0xFFAAAAAA;
            //int valueColor = 0xFF55FFFF;
            int valueColor = CactusSettings.get().accentColor.get().color();
            int lineHeight = 10;
            int spacing = 4;

            int maxY = y + boxHeight - 10;

            if (textY < maxY) {
                drawInfoLine(guiGraphics, "IP:", ServerInfoManager.getIp(), textX, textY, labelColor, valueColor, boxWidth - 20);
                textY += lineHeight * 2 + spacing;
            }

            if (textY < maxY) {
                drawInfoLine(guiGraphics, "Reverse:", ServerInfoManager.getReverse(), textX, textY, labelColor, valueColor, boxWidth - 20);
                textY += lineHeight * 2 + spacing;
            }

            if (textY < maxY) {
                guiGraphics.drawString(this.font, "Name Server:", textX, textY, labelColor, true);
                textY += lineHeight;
                String[] nameServers = ServerInfoManager.getNameServer().split(", ");
                for (String ns : nameServers) {
                    if (textY >= maxY) break;
                    guiGraphics.drawString(this.font, this.font.plainSubstrByWidth(ns.trim(), boxWidth - 20), textX, textY, valueColor, true);
                    textY += lineHeight;
                }
                textY += spacing;
            }

            if (textY < maxY) {
                drawInfoLine(guiGraphics, "ASN:", ServerInfoManager.getAsn(), textX, textY, labelColor, valueColor, boxWidth - 20);
                textY += lineHeight * 2 + spacing;
            }

            if (textY < maxY) {
                drawInfoLine(guiGraphics, "Net Name:", ServerInfoManager.getNetName(), textX, textY, labelColor, valueColor, boxWidth - 20);
                textY += lineHeight * 2 + spacing;
            }

            if (textY < maxY) {
                drawInfoLine(guiGraphics, "Net Description:", ServerInfoManager.getNetDesc(), textX, textY, labelColor, valueColor, boxWidth - 20);
                textY += lineHeight * 2 + spacing;
            }

            if (textY < maxY) {
                drawInfoLine(guiGraphics, "Organisation Name:", ServerInfoManager.getOrg(), textX, textY, labelColor, valueColor, boxWidth - 20);
            }
        }

    }

    private void drawInfoLine(GuiGraphics guiGraphics, String label, String value, int x, int y, int labelColor, int valueColor, int maxWidth) {
        if (V0IDUtility.serverInfo) {
            guiGraphics.drawString(this.font, label, x, y, labelColor, true);

            String displayValue = this.font.plainSubstrByWidth(value, maxWidth);
            guiGraphics.drawString(this.font, displayValue, x, y + 10, valueColor, true);
        }
    }

}
