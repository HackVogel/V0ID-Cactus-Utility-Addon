package dev.hackvogel.v0id.feature.modules;

import com.dwarslooper.cactus.client.event.EventHandler;
import com.dwarslooper.cactus.client.event.impl.ClientTickEvent;
import com.dwarslooper.cactus.client.feature.module.Module;
import com.dwarslooper.cactus.client.gui.toast.ToastSystem;
import com.dwarslooper.cactus.client.util.game.ChatUtils;
import dev.hackvogel.v0id.V0ID;
import dev.hackvogel.v0id.config.ConfigManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.w3c.dom.Text;

import static com.dwarslooper.cactus.client.feature.content.ContentPackManager.isEnabled;

public class AutoAuthorizeModule extends Module {

    String key = ConfigManager.getConfig().authKey;
    private boolean registered = false;

    public AutoAuthorizeModule() {
        super("AutoAuthorize", V0ID.CATEGORY, new Options());
    }

    @Override
    public void onEnable() {
        registered = false;

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!isEnabled()) return;
            handleMessage(message);
        });
    }

    @Override
    public void onDisable() {
        registered = false;
    }

    @EventHandler
    public void onTick(ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            try {
                java.lang.reflect.Field titleField = client.gui.getClass().getDeclaredField("title");
                java.lang.reflect.Field subtitleField = client.gui.getClass().getDeclaredField("subtitle");
                titleField.setAccessible(true);
                subtitleField.setAccessible(true);

                net.minecraft.network.chat.Component title = (net.minecraft.network.chat.Component) titleField.get(client.gui);
                net.minecraft.network.chat.Component subtitle = (net.minecraft.network.chat.Component) subtitleField.get(client.gui);

                String combined = "";
                if (title != null) combined += title.getString().toLowerCase();
                if (subtitle != null) combined += " " + subtitle.getString().toLowerCase();

                if (!combined.isBlank()) {
                    handleText(combined);
                }
            } catch (Exception ignored) {}
        });
    }

    private void handleMessage(Component message) {
        handleText(message.getString().toLowerCase());
    }

    private void handleText(String text) {
        if (text.contains("/register") || text.contains("/reg")) {
            if (!registered) {
                Minecraft.getInstance().player.connection.sendCommand("register " + key + " " + key);
                ChatUtils.infoPrefix("AutoAuthorize", "Registered.");
                ToastSystem.displayMessage("V0ID Addon", "Successfully registered!", Items.DEBUG_STICK, 5000);
                registered = true;
            }
        } else if (text.contains("/login") || text.contains("/log")) {
            Minecraft.getInstance().player.connection.sendCommand("login " + key);
            ChatUtils.infoPrefix("AutoAuthorize", "Logged in.");
            ToastSystem.displayMessage("V0ID Addon", "Successfully Logged in!", Items.DEBUG_STICK, 5000);
        }
    }
}