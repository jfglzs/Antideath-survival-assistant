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
    private static final List<IConfigBase> ALL = new ArrayList<>();
    private static final List<IConfigBase> LMS = new ArrayList<>();;
    private static final List<IConfigBase> DISABLES = new ArrayList<>();
    private static final List<IConfigBase> FUNCTIONS = new ArrayList<>();
    private static final List<IConfigBase> COMMANDS  = new ArrayList<>();

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
            else if (tab == Tab.LMS) LMS.add(obj);
            else if (tab == Tab.DISABLES) DISABLES.add(obj);
            else if (tab == Tab.COMMAND) COMMANDS.add(obj);
        }

        if (obj instanceof ConfigHotkey hotkey) {
            KEY_LIST.add(hotkey);
        }
        else if (obj instanceof ConfigBooleanHotkeyed booleanHotkeyed) {
            SWITCH_KEY.add(booleanHotkeyed);
        }
    }

    public static ImmutableList<IConfigBase> getConfigs(Tab tab) {
           return switch (tab) {
               case ALL -> ImmutableList.copyOf(ALL);
               case FUNCTIONS -> ImmutableList.copyOf(FUNCTIONS);
               case DISABLES -> ImmutableList.copyOf(DISABLES);
               case COMMAND -> ImmutableList.copyOf(COMMANDS);
               case LMS -> ImmutableList.copyOf(LMS);
           };
    }
}
