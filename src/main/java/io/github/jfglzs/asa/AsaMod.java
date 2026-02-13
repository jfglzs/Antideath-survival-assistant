package io.github.jfglzs.asa;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.config.HotkeysCallback;
import io.github.jfglzs.asa.config.InputHandler;
import io.github.jfglzs.asa.feature.creeperwarn.CreeperCheckClient;
import io.github.jfglzs.asa.feature.itemdisplay.RemainingItemDisplayer;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import io.github.jfglzs.asa.utils.ScreenUtils;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.jfglzs.asa.config.Configs.*;
import static io.github.jfglzs.asa.feature.materialrecycle.MaterialRecycler.*;
import static io.github.jfglzs.asa.utils.MCUtils.getPlayer;

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
        masaRegister();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            checkTime++;
            if (checkTime % 15  == 0 && DISPLAY_REMAIN_ITEM.getBooleanValue() && PlayerUtils.isNotAirInMainHand())
                MCUtils.ChatUtils.overLayMess(
                    String.format("Item: %s Remain: %d",
                            MCUtils.getMinecraftClient().player.getMainHandStack().getItem(),
                            RemainingItemDisplayer.checkRemainCount(MCUtils.getMinecraftClient().player.getMainHandStack().getItem())
                    )
                );
            
            if (checkTime % 20  == 0 && CREEPER_WARN.getBooleanValue()) creeperWarner();
            if (checkTime % 200 == 0 && MATERIAL_RECYCLER.getBooleanValue() && MATERIAL_RECYCLER_AUTO.getBooleanValue()) openBox();
            if (checkTime % 210 == 0 && MATERIAL_RECYCLER.getBooleanValue() && MATERIAL_RECYCLER_AUTO.getBooleanValue()) ScreenUtils.refreshScreen();
        });

	}

    private void masaRegister() {
        Configs.INSTANCE.load();
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, Configs.INSTANCE);
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        HotkeysCallback.init();
        LOGGER.info("Masa config loaded");
    }

    public static boolean shouldOpenBox(boolean screenCheck) {
        MinecraftClient client = MCUtils.getMinecraftClient();
        PlayerEntity player = getPlayer();
        if (player == null) return false;
        PlayerInventory inventory = player.getInventory();
        if (!(client.currentScreen == null) && screenCheck) return false;

        for (int i = 0; i < inventory.size() - 1; i++) {
            Item item = inventory.getStack(i).getItem();
            if (ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue() && !isBlackListed(item)) return true;
            if (isWhiteListed(item) && !ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue()) return true;
        }

        return false;
    }


    private static void creeperWarner() {
        if (CreeperCheckClient.isCreeperNearby()) {
            MCUtils.ChatUtils.sendMessWithSound("§c苦力怕来了!!!!!!!", SoundEvents.ENTITY_TNT_PRIMED , 1, 1);
        }
    }

    private static void openBox() {
        if (shouldOpenBox(true)) {
            openAllBoxes();
        }
    }

    public static void test() {
//        SystemInfo systemInfo = new SystemInfo();
//        CentralProcessor.ProcessorIdentifier hardware = systemInfo.getHardware().getProcessor().getProcessorIdentifier();
    }
}
