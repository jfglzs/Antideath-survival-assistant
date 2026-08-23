package io.github.jfglzs.asa.utils;


import io.github.jfglzs.asa.AsaMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
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
        return MCUtils.getMinecraft().level;
    }

    public static void executeCommand(String command) {
        AsaMod.debugMessage(() -> "Executing command: " + command);
        if (mc.player != null && command != null) {
            mc.player.connection.sendCommand(command.replaceFirst("/", ""));
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

    public static Screen getScreen() {
        //~ if >= 26.2 '.screen' -> '.gui.screen()' {
        return mc.screen;
        //~}
    }

    public static void setScreen(Screen screen) {
        //~ if >= 26.2 'setScreen' -> 'setScreenAndShow' {
        mc.setScreen(screen);
        //~}
    }

    public static boolean isPlayerOnline(String playerName) {
        ClientLevel level = MCUtils.getLevel();
        if (level == null) return false;
        for (AbstractClientPlayer clientPlayer : level.players()) {
            if (PlayerUtils.getName(clientPlayer).equalsIgnoreCase(playerName))
                return true;
        }
        return false;
    }

    public static void disconnect() {
        ClientLevel level = MCUtils.getLevel();
        if (level == null) return;
        //? if < 1.21.8 {
        /*level.disconnect();
         *///?} else if < 1.21.10 {
        /*level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
         *///?} else {
        MCUtils.getMinecraft().disconnectFromWorld(ClientLevel.DEFAULT_QUIT_MESSAGE);
        //?}
    }
}
