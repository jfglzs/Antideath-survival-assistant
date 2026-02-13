package xyz.antideath.asa.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.ScreenHandler;

public class ScreenUtils {
    static MinecraftClient client = MinecraftClient.getInstance();

    public static void refreshScreen() {
        openInventoryScreen();
        closeScreen();
    }

    public static void closeScreen() {
        if (client.player == null) return;
        client.player.closeHandledScreen();
    }

    public static void openInventoryScreen() {
        if (client.player == null) return;
        client.setScreen(new InventoryScreen(client.player));
    }

    public static ScreenHandler openAndGetHandle(Screen screen) {
        client.setScreen(screen);
        if (client.player == null) return null;
        return client.player.currentScreenHandler;
    }

    public static boolean openAndCheckScreen() {
        client = MCUtils.getMinecraftClient();
        if (client.currentScreen == null) openInventoryScreen();
        if (client.player == null || client.interactionManager == null) return false;
        ScreenHandler handler = client.player.currentScreenHandler;
        if (handler == null) return false;
        return true;
    }
}
