package io.github.jfglzs.asa.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.annotations.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigsManager {
    public static final List<IConfigBase> ALL = new ArrayList<>();
    public static final List<IConfigBase> LMS = new ArrayList<>();;
    public static final List<IConfigBase> DISABLES = new ArrayList<>();
    public static final List<IConfigBase> FUNCTIONS = new ArrayList<>();
    public static final List<IConfigBase> COMMANDS  = new ArrayList<>();

    public static final List<ConfigHotkey> KEY_LIST = new ArrayList<>();
    public static final List<IHotkeyTogglable> SWITCH_KEY = new ArrayList<>();

    public static void init() {
        for (Field field : Configs.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Config.class)) {
                Config config = field.getAnnotation(Config.class);
                addConfig(field, config);
            }
        }
    }

    public static void addConfig(Field field, Config config) {
        ConfigBase<?> obj;
        try {
            obj = (ConfigBase<?>) field.get(null);
        }
        catch (Exception e) {
            AsaMod.LOGGER.error("error while adding tab", e);
            return;
        }

        ALL.add(obj);
        for (Tab tab : config.tab()) {
            if (tab == Tab.FUNCTIONS) FUNCTIONS.add(obj);
            if (tab == Tab.LMS) LMS.add(obj);
            if (tab == Tab.DISABLES) DISABLES.add(obj);
            if (tab == Tab.COMMAND) COMMANDS.add(obj);
        }

        if (obj instanceof ConfigHotkey hotkey) {
            KEY_LIST.add(hotkey);
        }
        else if (obj instanceof ConfigBooleanHotkeyed booleanHotkeyed) {
            SWITCH_KEY.add(booleanHotkeyed);
        }
    }

    public static ImmutableList<IConfigBase> getConfigs(Tab tab) {
            if (tab == Tab.ALL) return ImmutableList.copyOf(ALL);
            else if (tab == Tab.FUNCTIONS) return ImmutableList.copyOf(FUNCTIONS);
            else if (tab == Tab.LMS) return ImmutableList.copyOf(LMS);
            else if (tab == Tab.DISABLES) return ImmutableList.copyOf(DISABLES);
            else return ImmutableList.copyOf(COMMANDS);
    }
}
