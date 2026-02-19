package dev.hackvogel.v0id.mixin;

import com.dwarslooper.cactus.client.gui.toast.ToastSystem;
import dev.hackvogel.v0id.screens.ChangelogScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Unique
    private static boolean V0id$hasLoadedOnce = false;

    @Inject(method = "init", at = @At("TAIL"))
    private void onMainMenuInit(CallbackInfo ci) {
        if (!V0id$hasLoadedOnce) {
            V0id$hasLoadedOnce = true;
            ToastSystem.displayMessage("V0ID Addon", "Successfully loaded!", Items.COMMAND_BLOCK, 5000);

            // Debugging

            ToastSystem.displayMessage("V0ID Addon", "Debugging-Mode activated!", Items.DEBUG_STICK, 15000);
            Minecraft.getInstance().setScreen(
                    new ChangelogScreen(Component.empty())
            );
        }
    }
}