package com.dwarslooper.cactus.addon_template.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ExampleMixin {

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(CallbackInfo info) {
		// This code is injected at the end of MinecraftClient.<init>
	}

}
