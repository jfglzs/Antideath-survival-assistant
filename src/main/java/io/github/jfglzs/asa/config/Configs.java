package io.github.jfglzs.asa.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.JsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.github.jfglzs.asa.AsaMod.MOD_ID;

public class Configs implements IConfigHandler {
    public static Configs INSTANCE = new Configs();
    public static boolean shouldDisableTitle = false;
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

    public static final ConfigStringList MATERIAL_RECYCLER_BLACK_LIST = new ConfigStringList( "材料回收助手黑名单",ImmutableList.of("dirt"),"自动回收材料的列表（黑名单）");
    public static final ConfigStringList MATERIAL_RECYCLER_LIST = new ConfigStringList( "材料回收助手白名单",ImmutableList.of("stone","dirt"),"自动回收材料的列表");
    public static final ConfigBooleanHotkeyed MATERIAL_RECYCLER = new ConfigBooleanHotkeyed( "材料回收助手", false,"","开启后打开潜影盒白名单列表的材料会被自动回收到背包中 \n ⚠材料回收助手只支持普通潜影盒 \n ⚠在快捷栏的盒子无效");
    public static final ConfigBooleanHotkeyed MATERIAL_RECYCLER_AUTO = new ConfigBooleanHotkeyed( "材料回收助手自动装盒", false,"","开启后 可以在有快捷潜影盒的服务器上实现自动装盒");
    public static final ConfigBoolean ENABLE_MATERIAL_RECYCLER_BLACK_LIST = new ConfigBoolean( "启用材料回收助手黑名单", false,"启用材料回收助手黑名单");

    public static final ConfigBooleanHotkeyed DISABLE_SUBTITLE = new ConfigBooleanHotkeyed( "打开材料列表时禁用字幕", false,"","打开投影的材料列表时禁用字幕");
    public static final ConfigBooleanHotkeyed DISABLE_CONNECT_TIMED_OUT = new ConfigBooleanHotkeyed( "禁用连接超时", false,"","此选项在投影加载原理图时可以阻止连接超时");
    public static final ConfigBooleanHotkeyed DISABLE_LOADING_TERRAIN_SCREEN = new ConfigBooleanHotkeyed( "禁用加载地形屏幕", false,"","开启后会禁用加载地形屏幕 理论上能提升一点点加入世界的速度(服务器同理)");
    public static final ConfigBooleanHotkeyed DISABLE_PLAYER_ARMOR_RENDER = new ConfigBooleanHotkeyed( "禁用玩家盔甲渲染", false,"","开启此功能后会禁用玩家的盔甲渲染\n终于可以看到涩涩的皮肤啦！");
    public static final ConfigBooleanHotkeyed DISPLAY_REMAIN_ITEM = new ConfigBooleanHotkeyed( "剩余物品显示", false,"","开启后会显示 主手中和背包中剩余的物品（包括潜影盒）");
    public static final ConfigBooleanHotkeyed DISABLE_PLACE_BLOCK_NEARBY_PORTAL = new ConfigBooleanHotkeyed( "禁止在地狱门周边放置方块", false,"","开启后禁会止在地狱门周边放置方块");
    public static final ConfigStringList DISABLE_PLACE_BLOCK_NEARBY_PORTAL_WHITELIST = new ConfigStringList( "禁止在地狱门周边放置方块白名单",ImmutableList.of(),"禁止在地狱门周边放置方块白名单");

    public static final ConfigBooleanHotkeyed TAP_FILTER = new ConfigBooleanHotkeyed( "TAB菜单过滤器", false,"","过滤掉tab菜单无用的玩家/常驻假人");
    public static final ConfigStringList TAP_FILTER_WHITELIST = new ConfigStringList( "TAB菜单过滤器-白名单",ImmutableList.of(),"");
    public static final ConfigBoolean ENABLE_TAP_FILTER_WHITELIST = new ConfigBoolean( "启用TAB菜单过滤器-白名单", false," ");
    public static final ConfigStringList TAP_FILTER_BLACKLIST = new ConfigStringList( "TAB菜单过滤器-黑名单",ImmutableList.of()," ");
    public static final ConfigBoolean ENABLE_TAP_FILTER_PREFIX = new ConfigBoolean( "启用TAB菜单过滤器-前缀", false," ");
    public static final ConfigStringList TAP_FILTER_PREFIX = new ConfigStringList( "TAB菜单过滤器-前缀",ImmutableList.of()," ");

    public static final ConfigStringList SWITCH_ITEM_LIST = new ConfigStringList( "切换物品列表",ImmutableList.of(),"切换物品列表");
    public static final ConfigHotkey SWITCH_ITEM = new ConfigHotkey( "切换物品", "","切换物品");

    public static final ConfigBooleanHotkeyed PREVENT_INTENTIONAL_GAME_DESIGN = new ConfigBooleanHotkeyed("防止被刻意的游戏设计杀死", false, "", "防止玩家被刻意的游戏设计杀死");

    public static final ConfigBooleanHotkeyed MINIHUD_POTIMIZE = new ConfigBooleanHotkeyed("优化Minihud的掉帧问题", false, "", "给updateLines()这个方法做了异步,使其不阻塞客户端线程,从而提升帧数");
    public static final ConfigBooleanHotkeyed LITEMATIC_LOAD_OPT = new ConfigBooleanHotkeyed("优化投影加载原理图的卡顿问题", false, "", "异步调用datafixer 优化加载原理图的卡顿感");


    public static final ConfigBooleanHotkeyed TEST = new ConfigBooleanHotkeyed( "mod调试", false,"测试", "", " ");


    public static final ImmutableList<IConfigBase> ALL = addCompatibility();
    public static ImmutableList<IConfigBase> addCompatibility() {
        List<IConfigBase> list = new ArrayList<>();
        list.add(ASA);

        list.add(CREEPER_WARN);
        list.add(CREEPER_WARN_RANGE);

        list.add(MATERIAL_RECYCLER);
        list.add(MATERIAL_RECYCLER_LIST);
        list.add(ENABLE_MATERIAL_RECYCLER_BLACK_LIST);
        list.add(MATERIAL_RECYCLER_BLACK_LIST);

        list.add(DISABLE_LOADING_TERRAIN_SCREEN);
        list.add(DISABLE_SUBTITLE);
        list.add(DISABLE_CONNECT_TIMED_OUT);
        list.add(DISABLE_PLAYER_ARMOR_RENDER);
        list.add(DISABLE_PLACE_BLOCK_NEARBY_PORTAL);
        list.add(DISABLE_PLACE_BLOCK_NEARBY_PORTAL_WHITELIST);

        list.add(MATERIAL_RECYCLER_AUTO);
        list.add(DISPLAY_REMAIN_ITEM);

        list.add(PREVENT_INTENTIONAL_GAME_DESIGN);
        list.add(SWITCH_SERVER);
        list.add(SWITCH_SERVER_COMMAND);

        list.add(SWITCH_SERVER_SINGLE_1);
        list.add(SWITCH_SERVER_SINGLE_1S);
        list.add(SWITCH_SERVER_SINGLE_2);
        list.add(SWITCH_SERVER_SINGLE_2S);
        list.add(SWITCH_SERVER_SINGLE_3);
        list.add(SWITCH_SERVER_SINGLE_3S);

        list.add(SWITCH_ITEM_LIST);
        list.add(SWITCH_ITEM);
        list.add(TAP_FILTER);
        list.add(ENABLE_TAP_FILTER_WHITELIST);
        list.add(TAP_FILTER_WHITELIST);
        list.add(TAP_FILTER_BLACKLIST);
        list.add(ENABLE_TAP_FILTER_PREFIX);
        list.add(TAP_FILTER_PREFIX);

        list.add(MINIHUD_POTIMIZE);
        list.add(LITEMATIC_LOAD_OPT);

        list.add(TEST);



        return ImmutableList.copyOf(list);
    }

    public static final ImmutableList<IHotkeyTogglable> SWITCH_KEY = ImmutableList.of(

            CREEPER_WARN,
            MATERIAL_RECYCLER,
            MATERIAL_RECYCLER_AUTO,
            DISABLE_LOADING_TERRAIN_SCREEN,
            DISABLE_SUBTITLE,
            DISABLE_PLAYER_ARMOR_RENDER,
            DISPLAY_REMAIN_ITEM,
            DISABLE_PLACE_BLOCK_NEARBY_PORTAL,
            PREVENT_INTENTIONAL_GAME_DESIGN,
            MINIHUD_POTIMIZE,
            LITEMATIC_LOAD_OPT

    );

    public static final ImmutableList<ConfigHotkey> KEY_LIST = ImmutableList.of(
            ASA,
            SWITCH_SERVER,
            SWITCH_SERVER_SINGLE_1,
            SWITCH_SERVER_SINGLE_2,
            SWITCH_SERVER_SINGLE_3,
            SWITCH_ITEM
    );

    public static final ImmutableList<IConfigBase> ALL_CONFIGS = addAllConfigs();
    public static ImmutableList<IConfigBase> addAllConfigs() {
        List<IConfigBase> list = new ArrayList<>();
        list.addAll(ALL);
        return ImmutableList.copyOf(list);
    }


    @Override
    public void load() {
        File settingFile = new File(FILE_PATH);
        if (settingFile.isFile() && settingFile.exists()) {
            JsonElement jsonElement = JsonUtils.parseJsonFile(settingFile);
            if (jsonElement != null && jsonElement.isJsonObject()) {
                JsonObject obj = jsonElement.getAsJsonObject();
                ConfigUtils.readConfigBase(obj, MOD_ID, ALL_CONFIGS);
            }
        }
    }

    @Override
    public void save() {
        if ((CONFIG_DIR.exists() && CONFIG_DIR.isDirectory()) || CONFIG_DIR.mkdirs()) {
            JsonObject configRoot = new JsonObject();
            ConfigUtils.writeConfigBase(configRoot, MOD_ID, ALL_CONFIGS);
            JsonUtils.writeJsonToFile(configRoot, new File(FILE_PATH));
        }
    }


    public static int getFeatureAmount() {
        return addCompatibility().size();
    }
}
