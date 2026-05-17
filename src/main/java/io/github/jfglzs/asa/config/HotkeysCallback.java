package io.github.jfglzs.asa.config;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.feature.switchserver.AsaSwitchServer;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.Item;

import static io.github.jfglzs.asa.config.Configs.*;

public class HotkeysCallback implements IHotkeyCallback {
    MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {
        if (key == ASA.getKeybind()) {
            client.setScreen(new ConfigUi());
            return true;
        }
        else if (key == SWITCH_SERVER.getKeybind()) {
            AsaSwitchServer.switchServer();
            return true;
        }
        else if (key == SWITCH_SERVER_SINGLE_1.getKeybind()) {
            AsaSwitchServer.switchServer(SWITCH_SERVER_SINGLE_1S.getStringValue());
            return true;
        }
        else if (key == SWITCH_SERVER_SINGLE_2.getKeybind()) {
            AsaSwitchServer.switchServer(SWITCH_SERVER_SINGLE_2S.getStringValue());
            return true;
        }
        else if (key == SWITCH_SERVER_SINGLE_3.getKeybind()) {
            AsaSwitchServer.switchServer(SWITCH_SERVER_SINGLE_3S.getStringValue());
            return true;
        }
        else if (key == CLEAR_MATERIAL_TODO_OVERLAY.getKeybind()) {
            MaterialToDoRenderer.INSTANCE.items.clear();
        }
        else if (key == MATERIAL_TODO_OVERLAY_BOT_SUPPORT.getKeybind()) {
            MaterialToDoRenderer.INSTANCE.getItem();
            return true;
        }
        else if (key == LMS_TAKE_ITEM.getKeybind()) {
            Configs.lockCreativeScreen = true;
            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
            return true;
        }
        else if (key == TEST.getKeybind()){
            AsaMod.test();
            return true;
        }
        
        return false;
    }

    public static void init() {
        HotkeysCallback hotkeysCallback = new HotkeysCallback();

        for (ConfigHotkey configHotkey : Configs.KEY_LIST) {
            configHotkey.getKeybind().setCallback(hotkeysCallback);
        }
    }
}
