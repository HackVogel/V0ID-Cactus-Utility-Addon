package com.dwarslooper.cactus.addon_template.feature.commands;

import com.dwarslooper.cactus.client.feature.command.Command;
import com.dwarslooper.cactus.client.util.game.ChatUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.SharedSuggestionProvider;

public class ExampleCommand extends Command {

	public ExampleCommand() {
		super("example");
	}

	@Override
	public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
		builder.then(argument("name", StringArgumentType.greedyString()).executes(context -> {
			ChatUtils.infoPrefix("Example Command", "Hello, " + StringArgumentType.getString(context, "name"));
			return SINGLE_SUCCESS;
		}));
	}

}
