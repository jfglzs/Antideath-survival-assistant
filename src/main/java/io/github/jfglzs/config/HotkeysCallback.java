package io.github.jfglzs.config;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;

import static io.github.jfglzs.config.Configs.*;
import static io.github.jfglzs.feature.switchserver.asaSwitchServer.switchServer;
import static io.github.jfglzs.utils.PlayerUtils.test;

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
        } else if (key == SWITCH_SERVER.getKeybind())
        {
            switchServer();
            return true;
        }else if (key == SWITCH_SERVER_SINGLE_1.getKeybind())
        {
            switchServer(SWITCH_SERVER_SINGLE_1S.getStringValue());
            return true;
        }else if (key == SWITCH_SERVER_SINGLE_2.getKeybind())
        {
            switchServer(SWITCH_SERVER_SINGLE_2S.getStringValue());
            return true;
        }else if (key == SWITCH_SERVER_SINGLE_3.getKeybind())
        {
            switchServer(SWITCH_SERVER_SINGLE_3S.getStringValue());
            return true;
        }else if (key == TEST.getKeybind()){
            test();
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
