package io.github.jfglzs.asa;

import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.RenderEventHandler;
import io.github.jfglzs.asa.accessor.ClientPacketListenerAccessor;
import io.github.jfglzs.asa.commands.AutoVaultCommand;
import io.github.jfglzs.asa.commands.PlayerManipulateCommand;
import io.github.jfglzs.asa.config.*;
import io.github.jfglzs.asa.events.ClientTickEvent;
import io.github.jfglzs.asa.feature.autoVault.AutoVaultExecutor;
import io.github.jfglzs.asa.feature.autoWasteClean.AutoWasteCleanProcessor;
import io.github.jfglzs.asa.feature.boxSplitter.BoxSplitter;
import io.github.jfglzs.asa.feature.creeperwarn.CreeperCheckClient;
import io.github.jfglzs.asa.feature.disablePacketKick.PacketHandler;
import io.github.jfglzs.asa.feature.lowHealthSendCommandOrChat.LowHealthSendCommandOrChat;
import io.github.jfglzs.asa.packet.PlaceEntityPayload;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.render.RemainingItemRender;
import io.github.jfglzs.asa.utils.*;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class AsaMod implements ModInitializer {
    public static final String MOD_ID_FANCY = "ASA";
    public static final String MOD_ID = "antideath-survival-assistant";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String MINECRAFT_VERSION = "26.2";
    public static String version;

    public static void debugMessage(Supplier<String> obj) {
        if (Configs.DEBUG.getBooleanValue()) {
            String s = obj.get();
            ChatUtils.clientMess(ChatUtils.c(s));
            LOGGER.info(s);
        }
    }

    public static void test() {
    }

    @Override
    public void onInitialize() {
        version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
        LOGGER.info("AsaMod v{} is being loading...", version);
        this.init();
    }


    private void init() {
        //~ if >= 26.1 'registerGameOverlayRenderer' -> 'registerInGameGuiRenderer' {
        ConfigsManager.init();
        Configs.INSTANCE.load();
        HotkeysCallback.init();
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, Configs.INSTANCE);
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        RenderEventHandler.getInstance().registerInGameGuiRenderer(MaterialToDoRenderer.INSTANCE);
        RenderEventHandler.getInstance().registerInGameGuiRenderer(RemainingItemRender.INSTANCE);
        InitializationHandler.getInstance().registerInitializationHandler(InitHandler.INSTANCE);
        ItemStorageDataManager.init();
        ThreadUtils.init();
        AutoWasteCleanProcessor.init();
        PacketHandler.init();
        Mods.init();
        this.registerEvents();
        this.registerCommands();
        //~}
    }

    private void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvent::onUpdate);
        ClientTickEvent.register(i -> true, this::testOnTick);
        ClientTickEvent.register(i -> true, client -> AutoVaultExecutor.tick());
        ClientTickEvent.register(i -> true, client -> BoxSplitter.tick());
        ClientTickEvent.register(i -> true, LowHealthSendCommandOrChat::tick);
        ClientTickEvent.register(i -> true, ItemStorageDataManager::scanMatchedPlayersAndInteract);
        ClientTickEvent.register(i -> i % 10 == 0 && Configs.DISPLAY_REMAIN_ITEM.getBooleanValue(), RemainingItemRender.INSTANCE::tick);
        ClientTickEvent.register(i -> i % 20 == 0 && Configs.CREEPER_WARN.getBooleanValue(), CreeperCheckClient::tick);
        ClientTickEvent.register(i -> i % 40 == 0 && Configs.ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue(), MaterialToDoRenderer.INSTANCE::tick);
        ClientTickEvent.register(i -> i % 20000 == 0 && Configs.LMS_FETCH_SUPPORT.getBooleanValue() && CommandUtils.canUseCommand("getStorageData"), client -> ItemStorageDataManager.reflushCache());
        ClientTickEvent.register(i -> i % 20 == 0 && Configs.OPT_ITEM_FRAME.getBooleanValue(), client -> {
            LocalPlayer player = MCUtils.getLocalPlayer();
            if (player != null) {
                ItemStack stack = PlayerUtils.getPlayerMainHandStack();
                if (! stack.is(Items.FILLED_MAP)) return;
                MapId mapId = stack.get(DataComponents.MAP_ID);
                if (mapId != null) {
                    ((ClientPacketListenerAccessor) player.connection).asa$getMaps().remove(mapId.id());
                }
            }
        });
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> {
            PlayerManipulateCommand.register(dispatcher);
            AutoVaultCommand.register(dispatcher);
        });
    }

    public void testOnTick(Minecraft client) {
    }
}
