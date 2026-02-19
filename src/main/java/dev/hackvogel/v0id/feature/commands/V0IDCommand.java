package dev.hackvogel.v0id.feature.commands;

import com.dwarslooper.cactus.client.feature.command.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.hackvogel.v0id.config.ConfigManager;
import dev.hackvogel.v0id.screens.ChangelogScreen;
import dev.hackvogel.v0id.utils.V0IDUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class V0IDCommand extends Command {
    public V0IDCommand() {
        super("void");
    }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {

        builder.then(literal("changelog")
                .executes(context -> {
                    Minecraft.getInstance().execute(() ->
                            Minecraft.getInstance().setScreen(
                                    new ChangelogScreen((Component) Minecraft.getInstance().screen)
                            )
                    );
                    return SINGLE_SUCCESS;
                })
        );

//        builder.then(literal("login")
//                .executes(context -> {
//                    String key = ConfigManager.getConfig().authKey;
//                    V0IDUtility.sendCommand("login " + key);
//                    return SINGLE_SUCCESS;
//                })
//        );
    }
}
