package dev.hackvogel.v0id.utils;

import net.minecraft.client.Minecraft;

public class V0IDUtility {

    public static boolean serverInfo = false;


    public static void sendCommand(String Command) {
        Minecraft.getInstance().getConnection().sendCommand(Command);
    }

}
