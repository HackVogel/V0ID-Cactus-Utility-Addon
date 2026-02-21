package dev.hackvogel.v0id.feature.modules;

import com.dwarslooper.cactus.client.feature.module.Module;
import dev.hackvogel.v0id.V0ID;
import dev.hackvogel.v0id.utils.V0IDUtility;

public class AdvancedServerWidgetModule extends Module {


    public AdvancedServerWidgetModule() {
        super("Server Widgets", V0ID.CATEGORY, new Options());
    }

    @Override
    public void onEnable() {
        V0IDUtility.serverInfo = true;
    }

    @Override
    public void onDisable() {
        V0IDUtility.serverInfo = false;
    }
}
