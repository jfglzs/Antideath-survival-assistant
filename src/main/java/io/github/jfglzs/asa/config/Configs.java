package io.github.jfglzs.asa.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
//~ if >= 26.1 '.JsonUtils' -> '.data.json.JsonUtils' {
import fi.dy.masa.malilib.util.data.json.JsonUtils;
import java.nio.file.Path;
//~}
import io.github.jfglzs.asa.annotations.Config;
import io.github.jfglzs.asa.config.options.AutoCleanWasteMode;
import io.github.jfglzs.asa.config.options.LowHealthSendMode;

import java.io.File;

import static io.github.jfglzs.asa.AsaMod.MOD_ID;

public class Configs implements IConfigHandler {
    public static Configs INSTANCE = new Configs();
    public static boolean shouldDisableTitle = false;
    public static boolean lockCreativeScreen = false;
    private static final String FILE_PATH = "./config/" + MOD_ID + ".json";
    private static final File CONFIG_DIR = new File("./config");

    @Config
    public static final ConfigHotkey ASA = new ConfigHotkey( "打开设置菜单", "Z,K","打开设置菜单快捷键");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed CREEPER_WARN = new ConfigBooleanHotkeyed( "苦力怕预警器", true,"","当玩家8x8x8(默认)的范围存在苦力怕时 自动预警");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigDouble CREEPER_WARN_RANGE = new ConfigDouble( "苦力怕预警器范围",8,0,64,"苦力怕预警器范围(以玩家为中心)");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_SUBTITLE = new ConfigBooleanHotkeyed( "打开材料列表时禁用字幕", false,"","打开投影的材料列表时禁用字幕");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_CONNECT_TIMED_OUT = new ConfigBooleanHotkeyed( "禁用连接超时", false,"","此选项在投影加载原理图时可以阻止连接超时");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_LOADING_TERRAIN_SCREEN = new ConfigBooleanHotkeyed( "禁用加载地形屏幕", false,"","开启后会禁用加载地形屏幕 理论上能提升一点点加入世界的速度(服务器同理)");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_PLAYER_ARMOR_RENDER = new ConfigBooleanHotkeyed( "禁用玩家盔甲渲染", false,"","开启此功能后会禁用玩家的盔甲渲染\n终于可以看到涩涩的皮肤啦！");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed DISPLAY_REMAIN_ITEM = new ConfigBooleanHotkeyed( "剩余物品显示OverLay", false,"","开启后会显示 主手中和背包中剩余的物品（包括潜影盒）");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigInteger DISPLAY_REMAIN_ITEM_OVERLAY_Y_OFFSET = new ConfigInteger("剩余物品显示OverLay-y偏移", 0);
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigInteger DISPLAY_REMAIN_ITEM_OVERLAY_X_OFFSET = new ConfigInteger("剩余物品显示OverLay-x偏移", 0);
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_PLACE_BLOCK_NEARBY_PORTAL = new ConfigBooleanHotkeyed( "禁止在地狱门周边放置方块", false,"","开启后禁会止在地狱门周边放置方块");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList DISABLE_PLACE_BLOCK_NEARBY_PORTAL_WHITELIST = new ConfigStringList( "禁止在地狱门周边放置方块白名单",ImmutableList.of(),"禁止在地狱门周边放置方块白名单");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed TAP_FILTER = new ConfigBooleanHotkeyed( "TAB菜单过滤器", false,"","过滤掉tab菜单无用的玩家/常驻假人");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList TAP_FILTER_WHITELIST = new ConfigStringList( "TAB菜单过滤器-白名单",ImmutableList.of(),"");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBoolean ENABLE_TAP_FILTER_WHITELIST = new ConfigBoolean( "启用TAB菜单过滤器-白名单", false," ");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList TAP_FILTER_BLACKLIST = new ConfigStringList( "TAB菜单过滤器-黑名单",ImmutableList.of()," ");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBoolean ENABLE_TAP_FILTER_PREFIX = new ConfigBoolean( "启用TAB菜单过滤器-前缀", false," ");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList TAP_FILTER_PREFIX = new ConfigStringList( "TAB菜单过滤器-前缀",ImmutableList.of()," ");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_MATERIAL_TODO_OVERLAY = new ConfigBooleanHotkeyed("启用材料待取Overlay", false, "", "启用后鼠标中键点击，原理图内方块(玩家背包内没有)，自动添加进待取列表");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigHotkey CLEAR_MATERIAL_TODO_OVERLAY = new ConfigHotkey("清除材料待取Overlay", "", "");
    @Config(tab = Tab.LMS)
    public static final ConfigHotkey LMS_TAKE_ITEM = new ConfigHotkey("打开假人取货菜单", "", "打开假人取货菜单");
    @Config(tab = Tab.LMS)
    public static final ConfigBoolean LMS_FETCH_SUPPORT = new ConfigBoolean("假人远程取货支持" , false,"需要lms carpet addition");
    @Config(tab = Tab.LMS)
    public static final ConfigBooleanHotkeyed LMS_FETCH_SUPPORT_PLACE_ = new ConfigBooleanHotkeyed("右键投影方块取货", false,"","右键投影方块取货");
    @Config(tab = Tab.LMS)
    public static final ConfigBooleanHotkeyed MID_CLICK_TAKE_ITEM = new ConfigBooleanHotkeyed("中键投影取货", false, "", "启用后鼠标中键点击，原理图内方块(玩家背包内没有)，会立即取货（需要lms carpet addition）\n shift+中键取1盒 \n中键取1组");
    @Config(tab = Tab.LMS)
    public static final ConfigBooleanHotkeyed AUTO_OPEN_FAKE_PLAYER_INV = new ConfigBooleanHotkeyed("假人取货自动打开假人背包", false, "", "自动打开假人背包");
    @Config(tab = Tab.LMS)
    public static final ConfigBooleanHotkeyed AUTO_OPEN_FAKE_PLAYER_INV_MODE = new ConfigBooleanHotkeyed("假人取货自动打开假人背包交互模式", false, "", "true为指令交互 false为主手右键交互");
    @Config(tab = Tab.LMS)
    public static final ConfigBooleanHotkeyed AUTO_KILL_FAKE_PLAYERS = new ConfigBooleanHotkeyed("假人远程取货自动下线假人", false, "", "");
    @Config(tab = Tab.LMS)
    public static final ConfigInteger AUTO_COOLDOWN = new ConfigInteger("假人远程取货自动打开背包/自动下线延迟", 100, 100, 1000, "单位ms", "");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigInteger MATERIAL_TODO_OVERLAY_Y_OFFSET = new ConfigInteger("材料待取OverLay-y偏移", 0);
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigInteger MATERIAL_TODO_OVERLAY_X_OFFSET = new ConfigInteger("材料待取OverLay-x偏移", 0);
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed PREVENT_INTENTIONAL_GAME_DESIGN = new ConfigBooleanHotkeyed("防止被刻意的游戏设计杀死", false, "", "防止玩家被刻意的游戏设计杀死");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_PLAYER_LIST_HUD_BACKGROUND = new ConfigBooleanHotkeyed("禁用TAB菜单背景", false, "", "禁用TAB菜单背景");;
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigHotkey ENABLE_FAKE_PLAYER_KILL_AURA = new ConfigHotkey("触发假人杀戮光环","","触发假人杀戮光环");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigString FAKE_PLAYER_KILL_AURA_PREFIX = new ConfigString("假人杀戮光环前缀","bot_","前缀");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigDouble FAKE_PLAYER_KILL_AURA_RANGE = new ConfigDouble( "假人杀戮光环范围",4,0,32,"假人杀戮光环范围(以玩家为中心)");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_FAKE_PLAYER_KILL_AURA_BLACKLIST = new ConfigBooleanHotkeyed("启用假人杀戮光环黑名单", false, "", "启用假人杀戮光环黑名单");;
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList FAKE_PLAYER_KILL_AURA_BLACKLIST = new ConfigStringList( "假人杀戮光环黑名单", ImmutableList.of(),"假人杀戮光环黑名单（仅对名单内玩家生效，精确匹配，忽略大小写）");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_FAKE_PLAYER_KILL_AURA_WHITELIST = new ConfigBooleanHotkeyed("启用假人杀戮光环白名单", false, "", "启用假人杀戮光环白名单");;
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigStringList FAKE_PLAYER_KILL_AURA_WHITELIST = new ConfigStringList( "假人杀戮光环白名单", ImmutableList.of(),"假人杀戮光环白名单（仅对名单内玩家生效，精确匹配，忽略大小写）");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed LOW_HEALTH_EXECUTE_OR_SEND = new ConfigBooleanHotkeyed("低生命值自动执行命令/发送聊天消息", false, "", "可自定义命令");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigFloat LOW_HEALTH_VALUE = new ConfigFloat( "生命值阈值",4,1,20,"生命值阈值");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigOptionList LOW_HEALTH_SEND_MODE = new ConfigOptionList( "发送模式", LowHealthSendMode.SEND_CHAT_MESSAGE);
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigString LOW_HEALTH_SEND_CONTENT = new ConfigString( "发送内容", "!s");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed MINI_HUD_FPS_OPT = new ConfigBooleanHotkeyed("MiniHud掉帧优化", false, "", "MiniHud掉帧优化");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed FORCE_BLOCK_BREAK_COOL_DOWN = new ConfigBooleanHotkeyed("强制方块挖掘冷却", false, "", "OMMC移植功能");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed FLAT_MINING = new ConfigBooleanHotkeyed( "平坦挖掘", false,"","OMMC移植功能");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed TRANSPARENT_ITEM_FRAME = new ConfigBooleanHotkeyed("透明展示框", false,"","需要关闭MoreCulling的自定义展示框渲染器才能正常工作");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_ITEM_ENTITY_MULPOSE = new ConfigBooleanHotkeyed("禁用掉落物旋转", false,"","禁用掉落物旋转");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_SUBTITLE_OVERLAY_BACKGROUND = new ConfigBooleanHotkeyed("禁用字幕背景", false,"","禁用字幕背景");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed CAN_ALWAYS_DISCONNECT = new ConfigBooleanHotkeyed("随时可以断开连接", false,"","为ReconfigScreen和ConnectScreen增加了退出按钮 可随时断开连接");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_CONTAINER_BACKGROUND = new ConfigBooleanHotkeyed("禁用容器背景渲染", false,"","禁用容器背景渲染");
    @Config(tab = Tab.COMMAND)
    public static final ConfigBooleanHotkeyed PLAYER_MANIPULATE_COMMAND = new ConfigBooleanHotkeyed("/pm 命令", false,"","支持批量操作假人 格式 \n /pm <前缀> <开始值> <结束值> <Action> \n /pm <开始值> <结束值> <Action>");
    @Config(tab = Tab.COMMAND)
    public static final ConfigInteger PLAYER_MANIPULATE_COMMAND_WAIT_TIME = new ConfigInteger("/pm 命令执行冷却", 10, 1, 1000, "每执行一个action后的等待时间 单位ms");
    @Config(tab = Tab.COMMAND)
    public static final ConfigString PLAYER_MANIPULATE_COMMAND_DEFAULT_PREFIX = new ConfigString("/pm 命令默认前缀","bot_");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed CHAT_MESSAGE_MAPPING = new ConfigBooleanHotkeyed("聊天消息映射", false,"","需要关闭MoreCulling的自定义展示框渲染器才能正常工作");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList CHAT_MESSAGE_MAPPING_LIST = new ConfigStringList("聊天消息映射列表", ImmutableList.of(), "可将聊天消息映射为命令 \n 格式: \n !s=spectator ");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN = new ConfigBooleanHotkeyed("启用自动垃圾清理",false,"","开启后可自动清理垃圾");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigHotkey SWITCH_CLEAN_MODE = new ConfigHotkey( "启用自动垃圾清理-切换模式", "","切换清理模式");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigOptionList AUTO_WASTE_CLEAN_MODE = new ConfigOptionList( "启用自动垃圾清理-清理模式", AutoCleanWasteMode.DROP);
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigHotkey SAVE_ITEMS = new ConfigHotkey( "启用自动垃圾清理-保存背包物品", ""," 将玩家背包物品保存至黑名单/白名单");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN_WHITELIST = new ConfigBooleanHotkeyed("启用自动垃圾清理白名单", false,"","");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList AUTO_WASTE_CLEAN_WHITELIST = new ConfigStringList( "自动垃圾清理-白名单", ImmutableList.of(),"自动垃圾清理白名单");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN_BLACKLIST = new ConfigBooleanHotkeyed("启用自动垃圾清理黑名单", false,"","");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList AUTO_WASTE_CLEAN_BLACKLIST = new ConfigStringList( "自动垃圾清理-黑名单", ImmutableList.of(),"自动垃圾清理黑名单");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed MOUNT_LOGGERS_ON_MINIHUD = new ConfigBooleanHotkeyed("挂载Logger信息到MiniHud", false,"","");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST = new ConfigBooleanHotkeyed("启用挂载Logger信息到MiniHud-白名单", false,"","");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigStringList MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST = new ConfigStringList( "挂载Logger信息到MiniHud-白名单", ImmutableList.of(),".contains(xxx)");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST = new ConfigBooleanHotkeyed("启用挂载Logger信息到MiniHud-黑名单", false,"","");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigStringList MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST = new ConfigStringList( "挂载Logger信息到MiniHud-黑名单", ImmutableList.of(),".contains(xxx)");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed CAN_OPEN_MUTI_PLAYER_SCREEN_ON_GAMING = new ConfigBooleanHotkeyed("可在游戏中打开多人游戏菜单", false,"","可在游戏中打开多人游戏菜单");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION = new ConfigBooleanHotkeyed("启用自定义方块碰撞箱",false,"", "开启后可自定义方块碰撞箱");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_WHITELIST = new ConfigBooleanHotkeyed("启用自定义方块碰撞箱白名单", false,"","启用自定义方块碰撞箱白名单");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList STRONG_BLOCK_COLLISION_WHITELIST = new ConfigStringList( "自定义方块碰撞箱-白名单", ImmutableList.of(),"自定义方块碰撞箱白名单");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST = new ConfigBooleanHotkeyed("启用自定义方块碰撞箱黑名单", false,"","启用自定义方块碰撞箱黑名单");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList STRONG_BLOCK_COLLISION_BLACKLIST = new ConfigStringList( "自定义方块碰撞箱-黑名单", ImmutableList.of(),"自定义方块碰撞箱黑名单");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed CUSTOM_LITEMATICA_BLOCK_REPLACE = new ConfigBooleanHotkeyed("启用自定义投影方块替换", false,"","启用自定义方块碰撞箱白名单");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList CUSTOM_LITEMATICA_BLOCK_REPLACE_LIST = new ConfigStringList( "自定义投影方块替换", ImmutableList.of(),"自定义投影方块替换名单 \n 格式: \n minecraft:item|minecraft:item1");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed AUTO_BOX_RESTROKE = new ConfigBooleanHotkeyed("启用自动盒子补货", false,"","启用自动盒子补货(需要开启tweakeroo的自动补货)");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_AUTO_BOX_RESTROKE_WHITELIST = new ConfigBooleanHotkeyed("启用自动盒子补货白名单", false,"","启用自动盒子补货白名单");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList AUTO_BOX_RESTROKE_WHITELIST = new ConfigStringList( "自动盒子补货白名单", ImmutableList.of(),"自动盒子补货白名单");
    @Config(tab = Tab.FUNCTIONS)
    public static final ConfigBooleanHotkeyed ENABLE_AUTO_BOX_RESTROKE_BLACKLIST = new ConfigBooleanHotkeyed("启用自动盒子补货黑名单", false,"","启用自动盒子补货黑名单");
    @Config(tab = Tab.LISTS)
    public static final ConfigStringList AUTO_BOX_RESTROKE_BLACKLIST = new ConfigStringList( "自动盒子补货黑名单", ImmutableList.of(),"自动盒子补货黑名单");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed CONFIRM_SCREEN_ALWAYS_YES = new ConfigBooleanHotkeyed( "禁用确认执行屏幕(1.21.10+)", false,"","禁用确认执行屏幕");
    @Config(tab = Tab.DISABLES)
    public static final ConfigBooleanHotkeyed DISABLE_PACKET_KICK = new ConfigBooleanHotkeyed( "禁用数据包踢出", false,"","开启后会阻止玩家因为数据包错误被踢出服务器\n此功能建议搭配ViaFabricPLus食用");
    @Config(tab = {Tab.DISABLES, Tab.OPTIMIZATIONS})
    public static final ConfigBooleanHotkeyed DISABLE_PROFILER = new ConfigBooleanHotkeyed( "禁用Profiler", false,"","禁用后可提升帧数但会导致饼图不可用");
    @Config(tab = Tab.OPTIMIZATIONS)
    public static final ConfigBooleanHotkeyed CLIENT_ENTITY_TICK_OPTIMIZATION = new ConfigBooleanHotkeyed( "客户端实体Tick优化", false,"","优化/剔除了一些计算 使得客户端更加流畅");
    @Config
    public static final ConfigBooleanHotkeyed DEBUG = new ConfigBooleanHotkeyed("调试", false,"","1111");
    @Config
    public static final ConfigHotkey TEST = new ConfigHotkey( "触发调试","","测试", "1111");

    @Override
    public void load() {
        File settingFile = new File(FILE_PATH);
        if (settingFile.isFile() && settingFile.exists()) {
            //~ if < 26.1 'settingFile.toPath()' -> 'settingFile' {
            JsonElement jsonElement = JsonUtils.parseJsonFile(settingFile.toPath());
            //~}
            if (jsonElement != null && jsonElement.isJsonObject()) {
                JsonObject obj = jsonElement.getAsJsonObject();
                ConfigUtils.readConfigBase(obj, MOD_ID, ConfigsManager.getConfigs(Tab.ALL));
            }
        }
    }

    @Override
    public void save() {
        if ((CONFIG_DIR.exists() && CONFIG_DIR.isDirectory()) || CONFIG_DIR.mkdirs()) {
            JsonObject configRoot = new JsonObject();
            ConfigUtils.writeConfigBase(configRoot, MOD_ID, ConfigsManager.getConfigs(Tab.ALL));
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
