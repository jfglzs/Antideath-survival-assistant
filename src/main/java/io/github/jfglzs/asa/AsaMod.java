package io.github.jfglzs.asa;

import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.RenderEventHandler;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.config.HotkeysCallback;
import io.github.jfglzs.asa.config.InputHandler;
import io.github.jfglzs.asa.feature.creeperwarn.CreeperCheckClient;
import io.github.jfglzs.asa.feature.itemdisplay.RemainingItemDisplayer;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.jfglzs.asa.config.Configs.*;

public class AsaMod implements ClientModInitializer {
    public static String version;
    public static final String SPACE = " ";
    public static final String MOD_ID = "asa";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static int checkTime = 0;

	@Override
	public void onInitializeClient() {
        version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();

        LOGGER.info("AsaMod v{} is being loading...", version);
        this.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            checkTime++;
            if (checkTime % 15 == 0 && DISPLAY_REMAIN_ITEM.getBooleanValue() && PlayerUtils.isNotAirInMainHand())
                MCUtils.ChatUtils.overLayMess(
                    String.format("Item: %s Remain: %d", MCUtils.getMinecraftClient().player.getMainHandStack().getItem(), RemainingItemDisplayer.checkRemainCount(MCUtils.getMinecraftClient().player.getMainHandStack().getItem()))
                );
            else if (checkTime % 20 == 0 && CREEPER_WARN.getBooleanValue()) {
                creeperWarner();
            }

            if (checkTime % 40 == 0 && client.player != null) {
                MaterialToDoRenderer.INSTANCE.update();
            }
        });

	}

    private void init() {
        Configs.INSTANCE.load();
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, Configs.INSTANCE);
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        HotkeysCallback.init();
        RenderEventHandler.getInstance().registerGameOverlayRenderer(MaterialToDoRenderer.INSTANCE);
        LOGGER.info("Masa config loaded");
    }

    private static void creeperWarner() {
        if (CreeperCheckClient.isCreeperNearby()) {
            MCUtils.ChatUtils.sendMessWithSound("§c苦力怕来了!!!!!!!", SoundEvents.ENTITY_TNT_PRIMED , 1, 1);
        }
    }


    public static void test() {
    }
}
