package io.github.jfglzs.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.github.jfglzs.AsaMod.MOD_ID;

public class Configs implements IConfigHandler
{
    public static Configs INSTANCE = new Configs();
    private static final String FILE_PATH = "./config/" + MOD_ID + ".json";
    private static final File CONFIG_DIR = new File("./config");

    public static final ConfigHotkey ASA = new ConfigHotkey( "打开设置菜单", "Z,K","打开设置菜单快捷键");

    public static final ConfigHotkey SWITCH_SERVER = new ConfigHotkey( "快速切换服务器", "F9","默认使用 /server 来切换服务器");
    public static final ConfigString SWITCH_SERVER_COMMAND = new ConfigString( "自定义切换服务器命令","server","自定义切换服务器命令");
    public static final ConfigStringList SWITCH_SERVER_LIST = new ConfigStringList( "服务器列表",ImmutableList.of("survival","creative"),"自动切换服务器时的服务器列表");

    public static final ConfigString SWITCH_SERVER_SINGLE_1S = new ConfigString( "指定切换服务器1","","指定切换服务器");
    public static final ConfigHotkey SWITCH_SERVER_SINGLE_1 = new ConfigHotkey( "切换到服务器1", "","切换到指定的服务器1");
    public static final ConfigString SWITCH_SERVER_SINGLE_2S = new ConfigString( "指定切换服务器2","","指定切换服务器");
    public static final ConfigHotkey SWITCH_SERVER_SINGLE_2 = new ConfigHotkey( "切换到服务器2", "","切换到指定的服务器2");
    public static final ConfigString SWITCH_SERVER_SINGLE_3S = new ConfigString( "指定切换服务器3","","指定切换服务器");
    public static final ConfigHotkey SWITCH_SERVER_SINGLE_3 = new ConfigHotkey( "切换到服务器3", "","切换到指定的服务器3");

    public static final ConfigBooleanHotkeyed CREEPER_WARN = new ConfigBooleanHotkeyed( "苦力怕预警器", true,"","当玩家8x8x8(默认)的范围存在苦力怕时 自动预警");
    public static final ConfigDouble CREEPER_WARN_RANGE = new ConfigDouble( "苦力怕预警器范围",8,0,64,"苦力怕预警器范围(以玩家为中心)");

    public static final ImmutableList<IConfigBase> ALL = addCompatibility();
    public static ImmutableList<IConfigBase> addCompatibility()
    {
        List<IConfigBase> list = new ArrayList<>();
        list.add(ASA);
        list.add(CREEPER_WARN);
        list.add(SWITCH_SERVER);
        list.add(SWITCH_SERVER_SINGLE_1);
        list.add(SWITCH_SERVER_SINGLE_2);
        list.add(SWITCH_SERVER_SINGLE_3);


        list.add(SWITCH_SERVER_LIST);
        list.add(CREEPER_WARN_RANGE);
        list.add(SWITCH_SERVER_COMMAND);
        list.add(SWITCH_SERVER_SINGLE_1S);
        list.add(SWITCH_SERVER_SINGLE_2S);
        list.add(SWITCH_SERVER_SINGLE_3S);




        return ImmutableList.copyOf(list);
    }

    public static final ImmutableList<IHotkeyTogglable> SWITCH_KEY = ImmutableList.of(

    );

    public static final ImmutableList<ConfigHotkey> KEY_LIST = ImmutableList.of(
            ASA,
            SWITCH_SERVER,
            SWITCH_SERVER_SINGLE_1,
            SWITCH_SERVER_SINGLE_2,
            SWITCH_SERVER_SINGLE_3
    );

    public static final ImmutableList<IConfigBase> ALL_CONFIGS = addAllConfigs();
    public static ImmutableList<IConfigBase> addAllConfigs(){
        List<IConfigBase> list = new ArrayList<>();
        list.addAll(ALL);

        return ImmutableList.copyOf(list);
    }


    @Override
    public void load()
    {
        File settingFile = new File(FILE_PATH);
        if (settingFile.isFile() && settingFile.exists())
        {
            JsonElement jsonElement = JsonUtils.parseJsonFile(settingFile);
            if (jsonElement != null && jsonElement.isJsonObject())
            {
                JsonObject obj = jsonElement.getAsJsonObject();
                ConfigUtils.readConfigBase(obj, MOD_ID, ALL_CONFIGS);
            }
        }
    }

    @Override
    public void save()
    {
        if ((CONFIG_DIR.exists() && CONFIG_DIR.isDirectory()) || CONFIG_DIR.mkdirs())
        {
            JsonObject configRoot = new JsonObject();
            ConfigUtils.writeConfigBase(configRoot, MOD_ID, ALL_CONFIGS);
            JsonUtils.writeJsonToFile(configRoot, new File(FILE_PATH));
        }
    }

}
