package io.github.jfglzs.asa.config;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.feature.autoWasteClean.AutoWasteCleanProcessor;
import io.github.jfglzs.asa.feature.fakePlayerKillAura.FakePlayerKillAura;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;

import static io.github.jfglzs.asa.config.Configs.*;

public class HotkeysCallback implements IHotkeyCallback {
    Minecraft client = Minecraft.getInstance();

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {
        if (key == ASA.getKeybind()) {
            MCUtils.setScreen(new ConfigUi());
            return true;
        }
        else if (key == CLEAR_MATERIAL_TODO_OVERLAY.getKeybind()) {
            MaterialToDoRenderer.INSTANCE.items.clear();
        }
        else if (key == LMS_TAKE_ITEM.getKeybind()) {
            Configs.lockCreativeScreen = true;
            MCUtils.setScreen(new CreativeModeInventoryScreen(this.client.player, this.client.player.connection.enabledFeatures(), this.client.options.operatorItemsTab().get()));
            return true;
        }
        else if (key == ENABLE_FAKE_PLAYER_KILL_AURA.getKeybind()) {
            FakePlayerKillAura.kill();
        }
        else if (key == SAVE_ITEMS.getKeybind()) {
            AutoWasteCleanProcessor.saveItemToList();
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
