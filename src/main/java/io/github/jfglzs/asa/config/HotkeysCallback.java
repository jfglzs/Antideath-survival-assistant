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
import io.github.jfglzs.asa.utils.ChatHelper;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.LocalPlayer;

import static io.github.jfglzs.asa.config.Configs.*;

public class HotkeysCallback implements IHotkeyCallback {
    public static void init() {
        HotkeysCallback hotkeysCallback = new HotkeysCallback();

        for (ConfigHotkey configHotkey : ConfigsManager.KEY_LIST) {
            configHotkey.getKeybind().setCallback(hotkeysCallback);
        }
    }

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {
        if (key == Functions.ASA.getKeybind()) {
            MCUtils.setScreen(new ConfigUi());
            return true;
        }
        else if (key == LMS.LMS_TAKE_ITEM.getKeybind()) {
            Configs.lockCreativeScreen = true;
            LocalPlayer player = MCUtils.getLocalPlayer();
            Minecraft mc = MCUtils.getMinecraft();
            MCUtils.setScreen(new CreativeModeInventoryScreen(player, player.connection.enabledFeatures(), mc.options.operatorItemsTab().get()));
            return true;
        }
        else if (key == Functions.ENABLE_FAKE_PLAYER_KILL_AURA.getKeybind()) {
            FakePlayerKillAura.kill();
            return true;
        }
        else if (key == Functions.SAVE_ITEMS.getKeybind()) {
            AutoWasteCleanProcessor.saveItemToList();
            return true;
        }
        else if (key == TEST.getKeybind()) {
            AsaMod.test();
            return true;
        }
        else if (key == Functions.SWITCH_CLEAN_MODE.getKeybind()) {
            Configs.switchMode(Functions.AUTO_WASTE_CLEAN_MODE);
            ChatHelper.actionBar(ChatHelper.c("g 自动垃圾清理： %s".formatted(Functions.AUTO_WASTE_CLEAN_MODE.getStringValue())));
            return true;
        }
        else if (key == Functions.TRIGGER_BOX_SPLITTER.getKeybind()) {
            BoxSplitter.addTask(PlayerUtils.getPlayerMainHandStack());
            return true;
        }
        else if (key == LMS.CLEAN_PLAYER_INV_CHACHE.getKeybind()) {
            ItemStorageDataManager.removeAll();
            return true;
        }
        return false;
    }
}
