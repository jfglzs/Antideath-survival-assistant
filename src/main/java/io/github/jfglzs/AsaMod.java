package io.github.jfglzs;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import io.github.jfglzs.config.Configs;
import io.github.jfglzs.config.HotkeysCallback;
import io.github.jfglzs.config.InputHandler;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.jfglzs.utils.CommandUtils.runCommand;

public class AsaMod implements ClientModInitializer {
    public static final String space = " ";
    public static final String MOD_ID = "ASA";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	@Override
	public void onInitializeClient() {
        LOGGER.info("AsaMod is loading...");
        masaRegister();
	}

    void masaRegister() {
        Configs.INSTANCE.load();
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, Configs.INSTANCE);
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        HotkeysCallback.init();
    }

}