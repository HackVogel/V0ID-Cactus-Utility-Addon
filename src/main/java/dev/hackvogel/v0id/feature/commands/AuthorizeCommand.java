package dev.hackvogel.v0id.feature.commands;

import com.dwarslooper.cactus.client.feature.command.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.hackvogel.v0id.config.ConfigManager;
import dev.hackvogel.v0id.utils.V0IDUtility;
import net.minecraft.commands.SharedSuggestionProvider;

public class AuthorizeCommand extends Command {

    public AuthorizeCommand() {
        super("authorize");
    }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {

        builder.then(literal("register")
                .executes(context -> {
                    String key = ConfigManager.getConfig().authKey;
                    V0IDUtility.sendCommand("register " + key + " " + key);
                    return SINGLE_SUCCESS;
                })
        );

        builder.then(literal("login")
                .executes(context -> {
                    String key = ConfigManager.getConfig().authKey;
                    V0IDUtility.sendCommand("login " + key);
                    return SINGLE_SUCCESS;
                })
        );
    }
}
