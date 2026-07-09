package io.github.jfglzs.asa.utils;


import io.github.jfglzs.asa.AsaMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
//~ if < 1.21.11 'Identifier' -> 'ResourceLocation' {
import net.minecraft.resources.Identifier;
//~}
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

    public static Block getBlock(String blockID) {
        String id = blockID;
        if (id.contains("minecraft")) id = id.replace("minecraft:", "");
        //? if >= 1.21.11 {
        var identifier = Identifier.withDefaultNamespace(id);
        //?} else {
        /*var identifier = ResourceLocation.withDefaultNamespace(id);
         *///?}
        //~ if <=1.21.1 '.getValue(' -> '.get(' {
        return BuiltInRegistries.BLOCK.getValue(identifier);
        //~}
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
