package io.github.jfglzs.asa.config;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.feature.autoWasteClean.AutoWasteCleanProcessor;
import io.github.jfglzs.asa.feature.boxSplitter.BoxSplitter;
import io.github.jfglzs.asa.feature.fakePlayerKillAura.FakePlayerKillAura;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
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
            return true;
        }
        else if (key == SAVE_ITEMS.getKeybind()) {
            AutoWasteCleanProcessor.saveItemToList();
            return true;
        }
        else if (key == TEST.getKeybind()){
            AsaMod.test();
            return true;
        }
        else if (key == SWITCH_CLEAN_MODE.getKeybind()) {
            Configs.switchMode(AUTO_WASTE_CLEAN_MODE);
            ChatUtils.sendOverLayMessage(ChatUtils.c("自动垃圾清理： %s".formatted(Configs.AUTO_WASTE_CLEAN_MODE.getStringValue())));
            return true;
        }
        else if (key == TRIGGER_BOX_SPLITTER.getKeybind()) {
            BoxSplitter.addTask(PlayerUtils.getPlayerMainHandStack());
        }
        else if (key == CLEAN_PLAYER_INV_CHACHE.getKeybind()) {
            ItemStorageDataManager.removeAll();
        }
        return false;
    }

    public static void init() {
        HotkeysCallback hotkeysCallback = new HotkeysCallback();

        for (ConfigHotkey configHotkey : ConfigsManager.KEY_LIST) {
            configHotkey.getKeybind().setCallback(hotkeysCallback);
        }
    }
}
