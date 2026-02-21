package dev.hackvogel.v0id.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DebugScreen extends Screen {
    public DebugScreen(Component title) {
        super(title);
    }

    @Override
    public void updateNarrationState(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, Component.literal("V0ID Debugger"));
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("Close"), btn -> {
            Minecraft.getInstance().setScreen(null);
        }).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Ext Srv Widget Screen"), btn -> {
            Minecraft.getInstance().setScreen(new ExtendedServerWidgetDebugScreen(Component.empty()));
        }).bounds(this.width / 2 - 60, this.height - 90, 120, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Changelog Screen"), btn -> {
            Minecraft.getInstance().setScreen(new ChangelogScreen(Component.empty()));
        }).bounds(this.width / 2 - 60, this.height - 60, 120, 20).build());

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(this.font, "V0ID Debugger", this.width / 2, 20, 0xfffc881f);
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

}