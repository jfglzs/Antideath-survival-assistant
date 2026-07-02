package io.github.jfglzs.asa.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.*;
//~ if >= 26.1 '.JsonUtils' -> '.data.json.JsonUtils' {
import fi.dy.masa.malilib.util.data.json.JsonUtils;
import java.nio.file.Path;
//~}
import io.github.jfglzs.asa.config.options.AutoCleanWasteMode;
import io.github.jfglzs.asa.config.options.LowHealthSendMode;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.jfglzs.asa.AsaMod.MOD_ID;

public class Configs implements IConfigHandler {
    public static Configs INSTANCE = new Configs();
    public static boolean shouldDisableTitle = false;
    public static boolean lockCreativeScreen = false;
    private static final String FILE_PATH = "./config/" + MOD_ID + ".json";
    private static final File CONFIG_DIR = new File("./config");

    public static final ConfigHotkey ASA = new ConfigHotkey( "打开设置菜单", "Z,K","打开设置菜单快捷键");

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
    public static final ConfigHotkey CLEAR_MATERIAL_TODO_OVERLAY = new ConfigHotkey("清除材料待取Overlay", "", "");

    public static final ConfigHotkey LMS_TAKE_ITEM = new ConfigHotkey("打开假人取货菜单", "", "打开假人取货菜单");

    public static final ConfigBoolean LMS_FETCH_SUPPORT = new ConfigBoolean("假人远程取货支持" , false,"需要lms carpet addition");
    public static final ConfigBooleanHotkeyed LMS_FETCH_SUPPORT_PLACE_ = new ConfigBooleanHotkeyed("右键投影方块取货", false,"","右键投影方块取货");
    public static final ConfigBooleanHotkeyed MID_CLICK_TAKE_ITEM = new ConfigBooleanHotkeyed("中键投影取货", false, "", "启用后鼠标中键点击，原理图内方块(玩家背包内没有)，会立即取货（需要lms carpet addition）");

    public static final ConfigBooleanHotkeyed AUTO_OPEN_FAKE_PLAYER_INV = new ConfigBooleanHotkeyed("假人取货自动打开假人背包", false, "", "自动打开假人背包");
    public static final ConfigBooleanHotkeyed AUTO_OPEN_FAKE_PLAYER_INV_MODE = new ConfigBooleanHotkeyed("假人取货自动打开假人背包交互模式", false, "", "true为指令交互 false为主手右键交互");

    public static final ConfigBooleanHotkeyed AUTO_KILL_FAKE_PLAYERS = new ConfigBooleanHotkeyed("假人远程取货自动下线假人", false, "", "");
    public static final ConfigInteger AUTO_COOLDOWN = new ConfigInteger("假人远程取货自动打开背包/自动下线延迟", 100, 100, 1000, "单位ms", "");

    public static final ConfigInteger MATERIAL_TODO_OVERLAY_Y_OFFSET = new ConfigInteger("材料待取OverLay-y偏移", 0);
    public static final ConfigInteger MATERIAL_TODO_OVERLAY_X_OFFSET = new ConfigInteger("材料待取OverLay-x偏移", 0);

    public static final ConfigBooleanHotkeyed PREVENT_INTENTIONAL_GAME_DESIGN = new ConfigBooleanHotkeyed("防止被刻意的游戏设计杀死", false, "", "防止玩家被刻意的游戏设计杀死");

    public static final ConfigBooleanHotkeyed DISABLE_PLAYER_LIST_HUD_BACKGROUND = new ConfigBooleanHotkeyed("禁用TAB菜单背景", false, "", "禁用TAB菜单背景");;

    public static final ConfigHotkey ENABLE_FAKE_PLAYER_KILL_AURA = new ConfigHotkey("触发假人杀戮光环","","触发假人杀戮光环");
    public static final ConfigString FAKE_PLAYER_KILL_AURA_PREFIX = new ConfigString("假人杀戮光环前缀","bot_","前缀");
    public static final ConfigDouble FAKE_PLAYER_KILL_AURA_RANGE = new ConfigDouble( "假人杀戮光环范围",4,0,32,"假人杀戮光环范围(以玩家为中心)");
    public static final ConfigBooleanHotkeyed ENABLE_FAKE_PLAYER_KILL_AURA_BLACKLIST = new ConfigBooleanHotkeyed("启用假人杀戮光环黑名单", false, "", "启用假人杀戮光环黑名单");;
    public static final ConfigStringList FAKE_PLAYER_KILL_AURA_BLACKLIST = new ConfigStringList( "假人杀戮光环黑名单", ImmutableList.of(),"假人杀戮光环黑名单（仅对名单内玩家生效，精确匹配，忽略大小写）");
    public static final ConfigBooleanHotkeyed ENABLE_FAKE_PLAYER_KILL_AURA_WHITELIST = new ConfigBooleanHotkeyed("启用假人杀戮光环白名单", false, "", "启用假人杀戮光环白名单");;
    public static final ConfigStringList FAKE_PLAYER_KILL_AURA_WHITELIST = new ConfigStringList( "假人杀戮光环白名单", ImmutableList.of(),"假人杀戮光环白名单（仅对名单内玩家生效，精确匹配，忽略大小写）");

    public static final ConfigBooleanHotkeyed LOW_HEALTH_EXECUTE_OR_SEND = new ConfigBooleanHotkeyed("低生命值自动执行命令/发送聊天消息", false, "", "可自定义命令");
    public static final ConfigFloat LOW_HEALTH_VALUE = new ConfigFloat( "生命值阈值",4,1,20,"生命值阈值");
    public static final ConfigOptionList LOW_HEALTH_SEND_MODE = new ConfigOptionList( "发送模式", LowHealthSendMode.SEND_CHAT_MESSAGE);
    public static final ConfigString LOW_HEALTH_SEND_CONTENT = new ConfigString( "发送内容", "!s");

    public static final ConfigBooleanHotkeyed MINI_HUD_FPS_OPT = new ConfigBooleanHotkeyed("MiniHud掉帧优化", false, "", "MiniHud掉帧优化");

    public static final ConfigBooleanHotkeyed FORCE_BLOCK_BREAK_COOL_DOWN = new ConfigBooleanHotkeyed("强制方块挖掘冷却", false, "", "OMMC移植功能");
    public static final ConfigBooleanHotkeyed FLAT_MINING = new ConfigBooleanHotkeyed( "平坦挖掘", false,"","OMMC移植功能");

    public static final ConfigBooleanHotkeyed TRANSPARENT_ITEM_FRAME = new ConfigBooleanHotkeyed("透明展示框", false,"","需要关闭MoreCulling的自定义展示框渲染器才能正常工作");
    public static final ConfigBooleanHotkeyed DISABLE_ITEM_ENTITY_MULPOSE = new ConfigBooleanHotkeyed("禁用掉落物旋转", false,"","禁用掉落物旋转");
    public static final ConfigBooleanHotkeyed DISABLE_SUBTITLE_OVERLAY_BACKGROUND = new ConfigBooleanHotkeyed("禁用字幕背景", false,"","禁用字幕背景");
    public static final ConfigBooleanHotkeyed CAN_ALWAYS_DISCONNECT = new ConfigBooleanHotkeyed("随时可以断开连接", false,"","为ReconfigScreen和ConnectScreen增加了退出按钮 可随时断开连接");
    public static final ConfigBooleanHotkeyed DISABLE_CONTAINER_BACKGROUND = new ConfigBooleanHotkeyed("禁用容器背景渲染", false,"","禁用容器背景渲染");

    public static final ConfigBooleanHotkeyed PLAYER_MANIPULATE_COMMAND = new ConfigBooleanHotkeyed("/pm 命令", false,"","支持批量操作假人 格式 \n /pm <前缀> <开始值> <结束值> <Action> \n /pm <开始值> <结束值> <Action>");
    public static final ConfigInteger PLAYER_MANIPULATE_COMMAND_WAIT_TIME = new ConfigInteger("/pm 命令执行冷却", 10, 1, 1000, "每执行一个action后的等待时间 单位ms");
    public static final ConfigString PLAYER_MANIPULATE_COMMAND_DEFAULT_PREFIX = new ConfigString("/pm 命令默认前缀","bot_");

    public static final ConfigBooleanHotkeyed CHAT_MESSAGE_MAPPING = new ConfigBooleanHotkeyed("聊天消息映射", false,"","需要关闭MoreCulling的自定义展示框渲染器才能正常工作");
    public static final ConfigStringList CHAT_MESSAGE_MAPPING_LIST = new ConfigStringList("聊天消息映射列表", ImmutableList.of(), "可将聊天消息映射为命令 \n 格式: \n !s=spectator ");

    public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN = new ConfigBooleanHotkeyed("启用自动垃圾清理",false,"开启后可自动清理垃圾");
    public static final ConfigHotkey SWITCH_CLEAN_MODE = new ConfigHotkey( "启用自动垃圾清理-切换模式", "","切换清理模式");
    public static final ConfigOptionList AUTO_WASTE_CLEAN_MODE = new ConfigOptionList( "启用自动垃圾清理-清理模式", AutoCleanWasteMode.DROP);
    public static final ConfigHotkey SAVE_ITEMS = new ConfigHotkey( "启用自动垃圾清理-保存背包物品", ""," 将玩家背包物品保存至黑名单/白名单");
    public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN_WHITELIST = new ConfigBooleanHotkeyed("启用自动垃圾清理白名单", false,"","");
    public static final ConfigStringList AUTO_WASTE_CLEAN_WHITELIST = new ConfigStringList( "自动垃圾清理-白名单", ImmutableList.of(),"自动垃圾清理白名单");
    public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN_BLACKLIST = new ConfigBooleanHotkeyed("启用自动垃圾清理黑名单", false,"","");
    public static final ConfigStringList AUTO_WASTE_CLEAN_BLACKLIST = new ConfigStringList( "自动垃圾清理-黑名单", ImmutableList.of(),"自动垃圾清理黑名单");

    public static final ConfigBooleanHotkeyed MOUNT_LOGGERS_ON_MINIHUD = new ConfigBooleanHotkeyed("挂载Logger信息到MiniHud", false,"","");
    public static final ConfigBooleanHotkeyed ENABLE_MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST = new ConfigBooleanHotkeyed("启用挂载Logger信息到MiniHud-白名单", false,"","");
    public static final ConfigStringList MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST = new ConfigStringList( "挂载Logger信息到MiniHud-白名单", ImmutableList.of(),".contains(xxx)");

    public static final ConfigBooleanHotkeyed ENABLE_MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST = new ConfigBooleanHotkeyed("启用挂载Logger信息到MiniHud-黑名单", false,"","");
    public static final ConfigStringList MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST = new ConfigStringList( "挂载Logger信息到MiniHud-黑名单", ImmutableList.of(),".contains(xxx)");

    public static final ConfigBooleanHotkeyed CAN_OPEN_MUTI_PLAYER_SCREEN_ON_GAMING = new ConfigBooleanHotkeyed("可在游戏中打开多人游戏菜单", false,"","可在游戏中打开多人游戏菜单");

    public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION = new ConfigBooleanHotkeyed("启用自定义方块碰撞箱",false,"开启后可自定义方块碰撞箱");
    public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_WHITELIST = new ConfigBooleanHotkeyed("启用自定义方块碰撞箱白名单", false,"","启用自定义方块碰撞箱白名单");
    public static final ConfigStringList STRONG_BLOCK_COLLISION_WHITELIST = new ConfigStringList( "自定义方块碰撞箱-白名单", ImmutableList.of(),"自定义方块碰撞箱白名单");
    public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST = new ConfigBooleanHotkeyed("启用自定义方块碰撞箱黑名单", false,"","启用自定义方块碰撞箱黑名单");
    public static final ConfigStringList STRONG_BLOCK_COLLISION_BLACKLIST = new ConfigStringList( "自定义方块碰撞箱-黑名单", ImmutableList.of(),"自定义方块碰撞箱黑名单");


    public static final ConfigBooleanHotkeyed DEBUG = new ConfigBooleanHotkeyed("调试", false,"","1111");
    public static final ConfigHotkey TEST = new ConfigHotkey( "触发调试","","测试", "1111");

    public static final ImmutableList<IConfigBase> ALL = addCompatibility();
    public static final ImmutableList<IConfigBase> LMS = addLMS();
    public static final ImmutableList<IConfigBase> DISABLES  = addDisables();
    public static final ImmutableList<IConfigBase> FUNCTIONS = addFunctions();
    public static final ImmutableList<IConfigBase> COMMANDS = addCommands();


    private static ImmutableList<IConfigBase> addCommands() {
        List<IConfigBase> list = new ArrayList<>();
        list.add(PLAYER_MANIPULATE_COMMAND);
        list.add(PLAYER_MANIPULATE_COMMAND_WAIT_TIME);
        list.add(PLAYER_MANIPULATE_COMMAND_DEFAULT_PREFIX);

        return ImmutableList.copyOf(list);
    }

    private static ImmutableList<IConfigBase> addFunctions() {
        List<IConfigBase> list = new ArrayList<>();

        list.add(FLAT_MINING);
        list.add(FORCE_BLOCK_BREAK_COOL_DOWN);

        list.add(LOW_HEALTH_EXECUTE_OR_SEND);
        list.add(LOW_HEALTH_VALUE);
        list.add(LOW_HEALTH_SEND_MODE);
        list.add(LOW_HEALTH_SEND_CONTENT);

        list.add(TAP_FILTER);
        list.add(ENABLE_TAP_FILTER_PREFIX);
        list.add(TAP_FILTER_PREFIX);
        list.add(ENABLE_TAP_FILTER_WHITELIST);
        list.add(TAP_FILTER_WHITELIST);
        list.add(TAP_FILTER_BLACKLIST);


        list.add(ENABLE_FAKE_PLAYER_KILL_AURA);
        list.add(FAKE_PLAYER_KILL_AURA_RANGE);
        list.add(FAKE_PLAYER_KILL_AURA_PREFIX);
        list.add(FAKE_PLAYER_KILL_AURA_WHITELIST);
        list.add(FAKE_PLAYER_KILL_AURA_BLACKLIST);

        list.add(ENABLE_MATERIAL_TODO_OVERLAY);
        list.add(MATERIAL_TODO_OVERLAY_X_OFFSET);
        list.add(MATERIAL_TODO_OVERLAY_Y_OFFSET);
        list.add(CLEAR_MATERIAL_TODO_OVERLAY);
        list.add(CHAT_MESSAGE_MAPPING);
        list.add(CHAT_MESSAGE_MAPPING_LIST);

        list.add(ENABLE_AUTO_WASTE_CLEAN);
        list.add(AUTO_WASTE_CLEAN_MODE);
        list.add(SAVE_ITEMS);
        list.add(ENABLE_AUTO_WASTE_CLEAN_WHITELIST);
        list.add(AUTO_WASTE_CLEAN_WHITELIST);
        list.add(ENABLE_AUTO_WASTE_CLEAN_BLACKLIST);
        list.add(AUTO_WASTE_CLEAN_BLACKLIST);

        list.add(MOUNT_LOGGERS_ON_MINIHUD);
        list.add(MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST);

        list.add(ENABLE_MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST);
        list.add(MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST);

        list.add(ENABLE_STRONG_BLOCK_COLLISION);
        list.add(ENABLE_STRONG_BLOCK_COLLISION_WHITELIST);
        list.add(STRONG_BLOCK_COLLISION_WHITELIST);
        list.add(ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST);
        list.add(STRONG_BLOCK_COLLISION_BLACKLIST);


        return ImmutableList.copyOf(list);
    }

    private static ImmutableList<IConfigBase> addDisables() {
        List<IConfigBase> list = new ArrayList<>();

        list.add(DISABLE_SUBTITLE);
        list.add(DISABLE_CONNECT_TIMED_OUT);
        list.add(DISABLE_ITEM_ENTITY_MULPOSE);
        list.add(DISABLE_PLAYER_ARMOR_RENDER);
        list.add(DISABLE_CONTAINER_BACKGROUND);
        list.add(DISABLE_PLACE_BLOCK_NEARBY_PORTAL);
        list.add(DISABLE_PLAYER_LIST_HUD_BACKGROUND);
        list.add(DISABLE_SUBTITLE_OVERLAY_BACKGROUND);
        list.add(TRANSPARENT_ITEM_FRAME);

        return ImmutableList.copyOf(list);
    }

    private static ImmutableList<IConfigBase> addLMS() {
        List<IConfigBase> list = new ArrayList<>();

        list.add(LMS_FETCH_SUPPORT);
        list.add(LMS_TAKE_ITEM);
        list.add(MID_CLICK_TAKE_ITEM);
        list.add(LMS_FETCH_SUPPORT_PLACE_);
        list.add(AUTO_OPEN_FAKE_PLAYER_INV);
        list.add(AUTO_OPEN_FAKE_PLAYER_INV_MODE);
        list.add(AUTO_KILL_FAKE_PLAYERS);
        list.add(AUTO_COOLDOWN);

        return ImmutableList.copyOf(list);
    }

    public static ImmutableList<IConfigBase> addCompatibility() {
        List<IConfigBase> list = new ArrayList<>();
        Arrays.stream(Configs.class.getDeclaredFields()).filter(field -> IConfigBase.class.isAssignableFrom(field.getType())).forEach(field -> {
            try {
                list.add((IConfigBase) field.get(null));
            }
            catch (Exception ignored) {
            }
        });
        return ImmutableList.copyOf(list);
    }

    public static final ImmutableList<IHotkeyTogglable> SWITCH_KEY = addSwitchKey();

    private static ImmutableList<IHotkeyTogglable> addSwitchKey() {
        List<IHotkeyTogglable> list = new ArrayList<>();
        Arrays.stream(Configs.class.getDeclaredFields()).filter(field -> ConfigBooleanHotkeyed.class.isAssignableFrom(field.getType())).forEach(field -> {
            try {
                list.add((IHotkeyTogglable) field.get(null));
            }
            catch (Exception ignored) {
            }
        });
        return ImmutableList.copyOf(list);
    }

    public static final ImmutableList<ConfigHotkey> KEY_LIST = addkey();

    private static ImmutableList<ConfigHotkey> addkey() {
        List<ConfigHotkey> list = new ArrayList<>();
        Arrays.stream(Configs.class.getDeclaredFields()).filter(field -> ConfigHotkey.class.isAssignableFrom(field.getType())).forEach(field -> {
            try {
                list.add((ConfigHotkey) field.get(null));
            }
            catch (Exception ignored) {
            }
        });
        return ImmutableList.copyOf(list);
    }

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
            //~ if < 26.1 'settingFile.toPath()' -> 'settingFile' {
            JsonElement jsonElement = JsonUtils.parseJsonFile(settingFile.toPath());
            //~}
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
            //? if < 26.1 {
            /*JsonUtils.writeJsonToFile(configRoot, new File(FILE_PATH));
            *///?} else {
            JsonUtils.writeJsonToFile(configRoot, Path.of(FILE_PATH));
            //?}
        }
    }

    public static void switchMode(ConfigOptionList option) {
        option.setOptionListValue(option.getOptionListValue().cycle(true));
    }
}
