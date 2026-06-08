package io.github.jfglzs.asa;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.RenderEventHandler;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.config.HotkeysCallback;
import io.github.jfglzs.asa.config.InputHandler;
import io.github.jfglzs.asa.events.ClientTickEvent;
import io.github.jfglzs.asa.feature.creeperwarn.CreeperCheckClient;
import io.github.jfglzs.asa.feature.lowHealthSendCommandOrChat.LowHealthSendCommandOrChat;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.render.RemainingItemRender;
import io.github.jfglzs.asa.utils.CommandUtils;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.jfglzs.asa.config.Configs.*;

//TODO 实现类似F3+F4切换服务器
public class AsaMod implements ClientModInitializer {
    public static String version;
    public static final String MOD_ID = "antideath-survival-assistant";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
        version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
        LOGGER.info("AsaMod v{} is being loading...", version);
        this.init();
	}

    private void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvent::onUpdate);
        ClientTickEvent.register(i -> true, LowHealthSendCommandOrChat::trigger);
        ClientTickEvent.register(i -> true, ItemStorageDataManager::scanMatchedPlayersAndInteract);
        ClientTickEvent.register(i -> i % 10 == 0 && DISPLAY_REMAIN_ITEM.getBooleanValue(), RemainingItemRender.INSTANCE::update);
        ClientTickEvent.register(i -> i % 20 == 0 && CREEPER_WARN.getBooleanValue(), CreeperCheckClient::creeperWarner);
        ClientTickEvent.register(i -> i % 40 == 0 && ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue(), MaterialToDoRenderer.INSTANCE::update);
        ClientTickEvent.register(i -> i % 1200 == 0 && LMS_FETCH_SUPPORT.getBooleanValue() && CommandUtils.canUseCommand("getStorageData"), client ->  ItemStorageDataManager.reflushCache());
    }

    //~ if >= 26.1 'registerGameOverlayRenderer' -> 'registerInGameGuiRenderer' {
    private void init() {
        ItemStorageDataManager.init();
        Configs.INSTANCE.load();
        HotkeysCallback.init();
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, INSTANCE);
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        RenderEventHandler.getInstance().registerInGameGuiRenderer(MaterialToDoRenderer.INSTANCE);
        RenderEventHandler.getInstance().registerInGameGuiRenderer(RemainingItemRender.INSTANCE);
        this.registerEvents();
        LOGGER.info("Masa registered");
    }
    //~}




    public static void test() {
    }
}
