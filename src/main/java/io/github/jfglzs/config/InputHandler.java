package io.github.jfglzs.config;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;
import io.github.jfglzs.AsaMod;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler
{
    private static final InputHandler INSTANCE = new InputHandler();

    @Override
    public void addKeysToMap(IKeybindManager manager)
    {
        //没被添加到的,按下按键时不会被识别
        for (IHotkey hotkey : Configs.KEY_LIST) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
        for (IHotkey hotkey : Configs.SWITCH_KEY) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager)
    {
        manager.addHotkeysForCategory(AsaMod.MOD_ID, "按下式",Configs.KEY_LIST );
        manager.addHotkeysForCategory(AsaMod.MOD_ID, "切换式",Configs.SWITCH_KEY);
    }

    public static InputHandler getInstance(){
        return INSTANCE;
    }
}
