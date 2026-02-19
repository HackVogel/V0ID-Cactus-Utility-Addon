package dev.hackvogel.v0id;

import com.dwarslooper.cactus.client.addon.v2.ICactusAddon;
import com.dwarslooper.cactus.client.addon.v2.RegistryBus;
import com.dwarslooper.cactus.client.feature.command.Command;
import com.dwarslooper.cactus.client.feature.module.Category;
import com.dwarslooper.cactus.client.feature.module.Module;
import com.dwarslooper.cactus.client.gui.toast.ToastSystem;
import dev.hackvogel.v0id.config.ConfigManager;
import dev.hackvogel.v0id.feature.commands.AuthorizeCommand;
import dev.hackvogel.v0id.feature.commands.ConfigCommand;
import dev.hackvogel.v0id.feature.commands.V0IDCommand;
import dev.hackvogel.v0id.feature.modules.AutoAuthorizeModule;

import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class V0ID implements ICactusAddon {

	public static final Logger LOGGER = LoggerFactory.getLogger("V0ID-CAC-ADDON");

	public static final Category CATEGORY = new Category("V0ID", Items.COMMAND_BLOCK.getDefaultInstance());

	@Override
	public void onInitialize(RegistryBus registryBus) {
		// This is called when the addon is initialized. It provides a RegistryBus
		// which will be used to register new features and content

		LOGGER.info("Hello from V0ID!");

		ConfigManager.loadConfig();

		// CATEGORY
		registryBus.register(Category.class, ctx -> CATEGORY);

		// MODULES
		registryBus.register(Module.class, ctx -> new AutoAuthorizeModule());

		// COMMANDS
		registryBus.register(Command.class, ctx -> new AuthorizeCommand());
		registryBus.register(Command.class, ctx -> new ConfigCommand());
		registryBus.register(Command.class, ctx -> new V0IDCommand());


		LOGGER.info("V0ID-CAC-ADDON successfully loaded!");
	}

	@Override
	public void onLoadComplete() {
		// This is called when Cactus is fully done initializing
		// This does not mean the game has completely loaded yet
	}

	@Override
	public void onShutdown() {
		// This is called when the client is shutting down
	}
}