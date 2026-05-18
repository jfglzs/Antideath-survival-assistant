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
    public static boolean lockCreativeScreen = false;
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
    public static final ConfigBooleanHotkeyed DISABLE_SUBTITLE = new ConfigBooleanHotkeyed( "打开材料列表时禁用字幕", false,"","打开投影的材料列表时禁用字幕");
    public static final ConfigBooleanHotkeyed DISABLE_CONNECT_TIMED_OUT = new ConfigBooleanHotkeyed( "禁用连接超时", false,"","此选项在投影加载原理图时可以阻止连接超时");
    public static final ConfigBooleanHotkeyed DISABLE_LOADING_TERRAIN_SCREEN = new ConfigBooleanHotkeyed( "禁用加载地形屏幕", false,"","开启后会禁用加载地形屏幕 理论上能提升一点点加入世界的速度(服务器同理)");
    public static final ConfigBooleanHotkeyed DISABLE_PLAYER_ARMOR_RENDER = new ConfigBooleanHotkeyed( "禁用玩家盔甲渲染", false,"","开启此功能后会禁用玩家的盔甲渲染\n终于可以看到涩涩的皮肤啦！");

    public static final ConfigBooleanHotkeyed DISPLAY_REMAIN_ITEM = new ConfigBooleanHotkeyed( "剩余物品显示OverLay", false,"","开启后会显示 主手中和背包中剩余的物品（包括潜影盒）");
    public static final ConfigInteger DISPLAY_REMAIN_ITEM_OVERLAY_Y_OFFSET = new ConfigInteger("剩余物品显示OverLay-y偏移", 0);
    public static final ConfigInteger DISPLAY_REMAIN_ITEM_OVERLAY_X_OFFSET = new ConfigInteger("剩余物品显示OverLay-x偏移", 0);

    public static final ConfigBooleanHotkeyed DISABLE_PLACE_BLOCK_NEARBY_PORTAL = new ConfigBooleanHotkeyed( "禁止在地狱门周边放置方块", false,"","开启后禁会止在地狱门周边放置方块");
    public static final ConfigStringList DISABLE_PLACE_BLOCK_NEARBY_PORTAL_WHITELIST = new ConfigStringList( "禁止在地狱门周边放置方块白名单",ImmutableList.of(),"禁止在地狱门周边放置方块白名单");

    public static final ConfigBooleanHotkeyed TAP_FILTER = new ConfigBooleanHotkeyed( "TAB菜单过滤器", false,"","过滤掉tab菜单无用的玩家/常驻假人");
    public static final ConfigStringList TAP_FILTER_WHITELIST = new ConfigStringList( "TAB菜单过滤器-白名单",ImmutableList.of(),"");
    public static final ConfigBoolean ENABLE_TAP_FILTER_WHITELIST = new ConfigBoolean( "启用TAB菜单过滤器-白名单", false," ");
    public static final ConfigStringList TAP_FILTER_BLACKLIST = new ConfigStringList( "TAB菜单过滤器-黑名单",ImmutableList.of()," ");
    public static final ConfigBoolean ENABLE_TAP_FILTER_PREFIX = new ConfigBoolean( "启用TAB菜单过滤器-前缀", false," ");
    public static final ConfigStringList TAP_FILTER_PREFIX = new ConfigStringList( "TAB菜单过滤器-前缀",ImmutableList.of()," ");

    public static final ConfigBooleanHotkeyed ENABLE_MATERIAL_TODO_OVERLAY = new ConfigBooleanHotkeyed("启用材料待取Overlay", false, "", "启用后鼠标中键点击，原理图内方块(玩家背包内没有)，自动添加进待取列表");
    public static final ConfigBooleanHotkeyed MATERIAL_TODO_OVERLAY_GET_ITEM_IMM = new ConfigBooleanHotkeyed("材料代取OverLay立即取货", false, "", "启用后鼠标中键点击，原理图内方块(玩家背包内没有)，会立即取货（需要lms carpet addition）");
    public static final ConfigHotkey CLEAR_MATERIAL_TODO_OVERLAY = new ConfigHotkey("清除材料待取Overlay", "", "");

    public static final ConfigHotkey LMS_TAKE_ITEM = new ConfigHotkey("打开假人取货菜单", "", "打开假人取货菜单");

    public static final ConfigHotkey MATERIAL_TODO_OVERLAY_BOT_SUPPORT = new ConfigHotkey("材料代取overlay假人取货支持", "", "需要lms carpet addition");
    public static final ConfigInteger MATERIAL_TODO_OVERLAY_BOT_FETCH = new ConfigInteger("假人取出数量", 64, "");

    public static final ConfigBoolean LMS_FETCH_SUPPORT = new ConfigBoolean("假人远程取货支持" , false,"需要lms carpet addition");
    public static final ConfigInteger MATERIAL_TODO_OVERLAY_Y_OFFSET = new ConfigInteger("材料待取OverLay-y偏移", 0);
    public static final ConfigInteger MATERIAL_TODO_OVERLAY_X_OFFSET = new ConfigInteger("材料待取OverLay-x偏移", 0);

    public static final ConfigBooleanHotkeyed PREVENT_INTENTIONAL_GAME_DESIGN = new ConfigBooleanHotkeyed("防止被刻意的游戏设计杀死", false, "", "防止玩家被刻意的游戏设计杀死");

    public static final ConfigBooleanHotkeyed TEST = new ConfigBooleanHotkeyed( "mod调试", false,"测试", "", " ");

    public static final ImmutableList<IConfigBase> ALL = addCompatibility();
    public static ImmutableList<IConfigBase> addCompatibility() {
        List<IConfigBase> list = new ArrayList<>();
        list.add(ASA);

        list.add(CREEPER_WARN);
        list.add(CREEPER_WARN_RANGE);

        list.add(DISABLE_LOADING_TERRAIN_SCREEN);
        list.add(DISABLE_SUBTITLE);
        list.add(DISABLE_CONNECT_TIMED_OUT);
        list.add(DISABLE_PLAYER_ARMOR_RENDER);
        list.add(DISABLE_PLACE_BLOCK_NEARBY_PORTAL);
        list.add(DISABLE_PLACE_BLOCK_NEARBY_PORTAL_WHITELIST);

        list.add(DISPLAY_REMAIN_ITEM);
        list.add(DISPLAY_REMAIN_ITEM_OVERLAY_X_OFFSET);
        list.add(DISPLAY_REMAIN_ITEM_OVERLAY_Y_OFFSET);

        list.add(PREVENT_INTENTIONAL_GAME_DESIGN);
        list.add(SWITCH_SERVER);
        list.add(SWITCH_SERVER_COMMAND);

        list.add(SWITCH_SERVER_SINGLE_1);
        list.add(SWITCH_SERVER_SINGLE_1S);
        list.add(SWITCH_SERVER_SINGLE_2);
        list.add(SWITCH_SERVER_SINGLE_2S);
        list.add(SWITCH_SERVER_SINGLE_3);
        list.add(SWITCH_SERVER_SINGLE_3S);

        list.add(TAP_FILTER);
        list.add(ENABLE_TAP_FILTER_WHITELIST);
        list.add(TAP_FILTER_WHITELIST);
        list.add(TAP_FILTER_BLACKLIST);
        list.add(ENABLE_TAP_FILTER_PREFIX);
        list.add(TAP_FILTER_PREFIX);

        list.add(ENABLE_MATERIAL_TODO_OVERLAY);
        list.add(MATERIAL_TODO_OVERLAY_GET_ITEM_IMM);
        list.add(CLEAR_MATERIAL_TODO_OVERLAY);
        list.add(MATERIAL_TODO_OVERLAY_X_OFFSET);
        list.add(MATERIAL_TODO_OVERLAY_Y_OFFSET);
        list.add(MATERIAL_TODO_OVERLAY_BOT_SUPPORT);
        list.add(MATERIAL_TODO_OVERLAY_BOT_FETCH);
        list.add(LMS_FETCH_SUPPORT);
        list.add(LMS_TAKE_ITEM);

        list.add(TEST);



        return ImmutableList.copyOf(list);
    }

    public static final ImmutableList<IHotkeyTogglable> SWITCH_KEY = ImmutableList.of(

            CREEPER_WARN,
            DISABLE_LOADING_TERRAIN_SCREEN,
            DISABLE_SUBTITLE,
            DISABLE_PLAYER_ARMOR_RENDER,
            DISPLAY_REMAIN_ITEM,
            DISABLE_PLACE_BLOCK_NEARBY_PORTAL,
            PREVENT_INTENTIONAL_GAME_DESIGN,
            ENABLE_MATERIAL_TODO_OVERLAY

    );

    public static final ImmutableList<ConfigHotkey> KEY_LIST = ImmutableList.of(
            ASA,
            SWITCH_SERVER,
            SWITCH_SERVER_SINGLE_1,
            SWITCH_SERVER_SINGLE_2,
            SWITCH_SERVER_SINGLE_3,
            CLEAR_MATERIAL_TODO_OVERLAY,
            MATERIAL_TODO_OVERLAY_BOT_SUPPORT,
            LMS_TAKE_ITEM

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
