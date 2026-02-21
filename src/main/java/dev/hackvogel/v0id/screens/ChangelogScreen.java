package dev.hackvogel.v0id.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3x2fStack;


public class ChangelogScreen extends Screen {
    public ChangelogScreen(Component title) {
        super(title);
    }

    @Override
    public void updateNarrationState(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, Component.literal("V0ID Changelog"));
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("Close"), btn -> {
            Minecraft.getInstance().setScreen(null);
        }).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(this.font, "V0ID Changelog", this.width / 2, 20, 0xfffc881f);

        String[] lines = {
                "v1.3  - Add Extended Server Widget Module (final release)",
                "b1.32 - Add Debug screen for Extended Server Widget",
                "b1.31 - Set widget color to Cactus ascent color",
                "b1.30 - Add Extended Server Widget Module",
                "v1.2  - Add debugMode | add ChangelogScreen",
                "v1.1  - Fix AutoAuthorizeModule",
                "v1.0  - Initial release"
        };

        int y = 50;
        for (String line : lines) {
            graphics.drawString(this.font, line, 20, y, 0xff02b50b);
            y += 12;
        }
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }


}