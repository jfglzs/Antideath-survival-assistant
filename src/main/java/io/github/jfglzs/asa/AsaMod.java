package io.github.jfglzs.asa;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.RenderEventHandler;
import io.github.jfglzs.asa.config.HotkeysCallback;
import io.github.jfglzs.asa.config.InputHandler;
import io.github.jfglzs.asa.feature.creeperwarn.CreeperCheckClient;
import io.github.jfglzs.asa.feature.lowHealthSendCommandOrChat.LowHealthSendCommandOrChat;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.render.RemainingItemRender;
import io.github.jfglzs.asa.utils.CommandUtils;
import io.github.jfglzs.asa.utils.lms.ItemStorageDataManager;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sounds.SoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.jfglzs.asa.config.Configs.*;

//TODO 实现类似F3+F4切换服务器
public class AsaMod implements ClientModInitializer {
    public static String version;
    public static final String MOD_ID = "antideath-survival-assistant";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static int checkTime = 0;

	@Override
	public void onInitializeClient() {
        version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();

        LOGGER.info("AsaMod v{} is being loading...", version);
        this.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            LowHealthSendCommandOrChat.trigger(client);
            ItemStorageDataManager.scanPlayers();

            if (checkTime % 10 == 0 && DISPLAY_REMAIN_ITEM.getBooleanValue()) {
                RemainingItemRender.INSTANCE.stack = PlayerUtils.getPlayerMainHandStack();
            }
            if (checkTime % 20 == 0 && CREEPER_WARN.getBooleanValue()) {
                creeperWarner();
            }
            if (checkTime % 200 == 0 && LMS_FETCH_SUPPORT.getBooleanValue() && CommandUtils.canUseCommand("getStorageData")) {
                ItemStorageDataManager.reflushCache();
            }
            if (checkTime % 40 == 0 && client.player != null && ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue()) {
                MaterialToDoRenderer.INSTANCE.update();
            }
        });

	}

    private void init() {
        ItemStorageDataManager.init();
        INSTANCE.load();
        HotkeysCallback.init();
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, INSTANCE);
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        RenderEventHandler.getInstance().registerGameOverlayRenderer(MaterialToDoRenderer.INSTANCE);
        RenderEventHandler.getInstance().registerGameOverlayRenderer(RemainingItemRender.INSTANCE);
        LOGGER.info("Masa registered");
    }

    private static void creeperWarner() {
        if (CreeperCheckClient.isCreeperNearby()) {
            MCUtils.ChatUtils.sendMessWithSound("§c苦力怕来了!!!!!!!", SoundEvents.TNT_PRIMED, 1, 1);
        }
    }


    public static void test() {
    }
}
