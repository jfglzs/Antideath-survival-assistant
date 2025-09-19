package io.github.jfglzs;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import io.github.jfglzs.config.Configs;
import io.github.jfglzs.config.HotkeysCallback;
import io.github.jfglzs.config.InputHandler;
import io.github.jfglzs.feature.creeperwarn.CreeperCheckClient;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.jfglzs.config.Configs.*;
import static io.github.jfglzs.feature.materialrecycle.MaterialRecycler.*;
import static io.github.jfglzs.utils.ChatUtils.sendMessWithTNTPRIMESound;
import static io.github.jfglzs.utils.MCUtils.getMinecraftClient;
import static io.github.jfglzs.utils.MCUtils.getPlayer;

public class AsaMod implements ClientModInitializer
{
    public static final String SPACE = " ";
    public static final String MOD_ID = "ASA";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static int checktime = 0;

	@Override
	public void onInitializeClient()
    {
        LOGGER.info("AsaMod is loading...");
        masaRegister();
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            checktime++;
            if (checktime % 20 == 0 && CREEPER_WARN.getBooleanValue() && CreeperCheckClient.isCreeperNearby()) sendMessWithTNTPRIMESound("§c苦力怕来了!!!!!!!" , 1 , 1);
            if (checktime % 120 == 0 && MATERIAL_RECYCLER.getBooleanValue() && shouldOpenBox()) OpenAllBoxes();
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

    public static boolean shouldOpenBox()
    {
        MinecraftClient client = getMinecraftClient();
        PlayerEntity player = getPlayer();

        if (player == null) return false;
        if (client == null) return false;

        PlayerInventory inventory = player.getInventory();

        if (!(client.currentScreen == null)) return false;

        for (int i = 0; i < inventory.size() - 1; i++)
        {
            Item item = inventory.getStack(i).getItem();
            if (ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue() && !isBlackListed(item)) return true;
            if (isWhiteListed(item) && !ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue()) return true;
        }

        return false;
    }
}