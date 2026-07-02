package io.github.jfglzs.asa.utils;


import io.github.jfglzs.asa.AsaMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public class MCUtils {
    private static final Minecraft mc = Minecraft.getInstance();

    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public static LocalPlayer getLocalPlayer() {
        return getMinecraft().player;
    }

    public static ClientLevel getLevel() {
       return (ClientLevel) getLocalPlayer().level();
    }

    public static void executeCommand(String command) {
        AsaMod.debugMessage("Executing command: " + command);
        if (mc.player != null && command != null) {
            mc.player.connection.sendCommand(command);
        }
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static String getItemID(Item item) {
        var id = BuiltInRegistries.ITEM.getKey(item);
        return id.toString();
    }

    public static String getBlockID(Block block) {
        var id = BuiltInRegistries.BLOCK.getKey(block);
        return id.toString();
    }

    public static void setScreen(Screen screen) {
        //~ if >= 26.2 'setScreen' -> 'setScreenAndShow' {
        mc.setScreen(screen);
        //~}
    }

    public static Screen getScreen() {
        //~ if >= 26.2 '.screen' -> '.gui.screen()' {
        return mc.screen;
        //~}
    }
}
