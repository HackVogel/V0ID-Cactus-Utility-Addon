package dev.hackvogel.v0id.mixin.screen;

import com.dwarslooper.cactus.client.gui.toast.ToastSystem;
import dev.hackvogel.v0id.config.ConfigManager;
import dev.hackvogel.v0id.screens.DebugScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screens.Screen;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen{

    @Unique
    private static boolean V0id$hasLoadedOnce = false;

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onMainMenuInit(CallbackInfo ci) {
        if (!V0id$hasLoadedOnce) {
            V0id$hasLoadedOnce = true;
            ToastSystem.displayMessage("V0ID Addon", "Successfully loaded!", Items.COMMAND_BLOCK, 5000);

            ConfigManager.loadConfig();
            if (ConfigManager.getConfig().debugMode.equals("true")) {
                ToastSystem.displayMessage("V0ID Addon", "Debugging-Mode activated!", Items.DEBUG_STICK, 15000);
                Minecraft.getInstance().setScreen(
                        new DebugScreen(Component.empty())
                );
            }
        }
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo info) {
        ConfigManager.loadConfig();
        if (ConfigManager.getConfig().debugMode.equals("true")) {
            this.addRenderableWidget(Button.builder(Component.literal("V"), btn -> {
                Minecraft.getInstance().setScreen(new DebugScreen(Component.empty()));
            }).bounds(this.width / 2 - 149, this.height / 4 + 132, 20, 20).build());
        }
    }

}