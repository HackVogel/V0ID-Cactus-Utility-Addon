package dev.hackvogel.v0id.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3x2fStack;


public class ChangelogScreen extends Screen {
    public ChangelogScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(this.font, "V0ID Changelog", this.width / 2, 20, 0xfffc881f);

        String[] lines = {
                "v1.1 - Fix AutoAuthorizeModule",
                "v1.0 - Initial release"
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