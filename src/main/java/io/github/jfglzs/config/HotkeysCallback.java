package io.github.jfglzs.config;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import io.github.jfglzs.AsaMod;
import io.github.jfglzs.feature.itemtaker.TriggerItemTaker;
import io.github.jfglzs.feature.switchserver.asaSwitchServer;
import net.minecraft.client.MinecraftClient;

import static io.github.jfglzs.AsaMod.test;
import static io.github.jfglzs.config.Configs.*;

public class HotkeysCallback implements IHotkeyCallback
{
    MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key)
    {
        if (key == ASA.getKeybind())
        {
            client.setScreen(new ConfigUi());
            return true;
        }
        else if (key == SWITCH_SERVER.getKeybind())
        {
            asaSwitchServer.switchServer();
            return true;
        }
        else if (key == SWITCH_SERVER_SINGLE_1.getKeybind())
        {
            asaSwitchServer.switchServer(SWITCH_SERVER_SINGLE_1S.getStringValue());
            return true;
        }
        else if (key == SWITCH_SERVER_SINGLE_2.getKeybind())
        {
            asaSwitchServer.switchServer(SWITCH_SERVER_SINGLE_2S.getStringValue());
            return true;
        }
        else if (key == SWITCH_SERVER_SINGLE_3.getKeybind())
        {
            asaSwitchServer.switchServer(SWITCH_SERVER_SINGLE_3S.getStringValue());
            return true;
        }
        else if (key == SWITCH_ITEM.getKeybind()){
            TriggerItemTaker.trigger();
            return true;
        }
        else if (key == TEST.getKeybind()){
            AsaMod.test();
            return true;
        }
        
        return false;
    }

    public static void init()
    {
        HotkeysCallback hotkeysCallback = new HotkeysCallback();

        for (ConfigHotkey configHotkey : Configs.KEY_LIST)
        {
            configHotkey.getKeybind().setCallback(hotkeysCallback);
        }
    }
}
