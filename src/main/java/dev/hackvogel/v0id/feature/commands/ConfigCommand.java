package dev.hackvogel.v0id.feature.commands;

import com.dwarslooper.cactus.client.feature.command.Command;
import com.dwarslooper.cactus.client.gui.toast.ToastSystem;
import com.dwarslooper.cactus.client.util.game.ChatUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.hackvogel.v0id.config.ConfigManager;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.item.Items;

import java.util.Random;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config");
    }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.then(literal("set")
                .then(literal("authKey")
                        .then(literal("generate")
                                .executes(context -> {
                                    String newKey = generateRandomString(16);
                                    ConfigManager.getConfig().authKey = newKey;
                                    ConfigManager.saveConfig();
                                    ChatUtils.infoPrefix("Config", "AuthKey generated: " + newKey);
                                    return SINGLE_SUCCESS;
                                })
                        )
                        .then(argument("key", StringArgumentType.string())
                                .executes(context -> {
                                    String newKey = StringArgumentType.getString(context, "key");
                                    ConfigManager.getConfig().authKey = newKey;
                                    ConfigManager.saveConfig();
                                    ChatUtils.infoPrefix("Config", "AuthKey updated to: " + newKey);
                                    return SINGLE_SUCCESS;
                                })
                        )
                )
                .then(literal("debugmode")
                        .then(argument("true", StringArgumentType.string())
                                .executes(context -> {
                                    String newMode = StringArgumentType.getString(context, "true");
                                    ConfigManager.getConfig().debugMode = newMode;
                                    ConfigManager.saveConfig();
                                    ChatUtils.infoPrefix("Config", "DebugMode updated to: " + newMode);
                                    ToastSystem.displayMessage("V0ID Addon", "Debugging-Mode activated!", Items.DEBUG_STICK, 15000);
                                    return SINGLE_SUCCESS;
                                })
                        )
                        .then(argument("false", StringArgumentType.string())
                                .executes(context -> {
                                    String newMode = StringArgumentType.getString(context, "false");
                                    ConfigManager.getConfig().debugMode = newMode;
                                    ConfigManager.saveConfig();
                                    ChatUtils.infoPrefix("Config", "DebugMode updated to: " + newMode);
                                    return SINGLE_SUCCESS;
                                })
                        )
                )
        );


        builder.then(literal("reload")
                .executes(context -> {
                    ConfigManager.loadConfig();
                    ChatUtils.infoPrefix("Config", "Config reloaded!");
                    return SINGLE_SUCCESS;
                })
        );
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
