package io.github.jfglzs;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import io.github.jfglzs.config.Configs;
import io.github.jfglzs.config.HotkeysCallback;
import io.github.jfglzs.config.InputHandler;
import io.github.jfglzs.feature.creeperWarner.CreeperCheckClient;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.sound.SoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.jfglzs.config.Configs.CREEPER_WARN;
import static io.github.jfglzs.config.Configs.ELYTRA_WARN;
import static io.github.jfglzs.feature.elytraWarn.ElytraDurationWarn.warnPlayer;
import static io.github.jfglzs.utils.ChatUtils.sendMessWithSound;

public class AsaMod implements ClientModInitializer
{
    public static final String SPACE = " ";
    public static final String MOD_ID = "ASA";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static int CHECKTIME = 0;

	@Override
	public void onInitializeClient()
    {
        LOGGER.info("AsaMod is loading...");
        masaRegister();
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            CHECKTIME++;
            if (CHECKTIME % 20 == 0){
                if (CREEPER_WARN.getBooleanValue()) if (CreeperCheckClient.isCreeperNearby()) sendMessWithSound("§c苦力怕来了!" , SoundEvents.ENTITY_TNT_PRIMED ,1, 1);
                if (ELYTRA_WARN.getBooleanValue()) warnPlayer();
            }

        });
	}

    void masaRegister()
    {
        Configs.INSTANCE.load();
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, Configs.INSTANCE);
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        HotkeysCallback.init();
    }
}