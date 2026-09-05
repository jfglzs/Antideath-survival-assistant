package io.github.jfglzs.asa.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.annotations.Config;
import io.github.jfglzs.asa.config.options.AutoCleanWasteMode;
import io.github.jfglzs.asa.config.options.ItemFrameVisibility;
import io.github.jfglzs.asa.config.options.OpenFakePlayerInvMode;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

//~ if >= 26.1 '.JsonUtils' -> '.data.json.JsonUtils' {
import fi.dy.masa.malilib.util.data.json.JsonUtils;
//~}

public class Configs implements IConfigHandler {
    public static final Configs INSTANCE = new Configs();
    public static final String ALL = AsaMod.MOD_ID + ".config.all";
    public static final Class<?>[] classes = new Class[]{Functions.class, Disables.class, Commands.class, Optimizations.class, LMS.class, Lists.class, Configs.class};

    public static class Disables {
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_SUBTITLE = new ConfigBooleanHotkeyed("disableSubtitle", false, "", "打开投影的材料列表时禁用字幕").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_CONNECT_TIMED_OUT = new ConfigBooleanHotkeyed("disableConnectionTimedOut", false, "", "此选项在投影加载原理图时可以阻止连接超时").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_LOADING_TERRAIN_SCREEN = new ConfigBooleanHotkeyed("disableLevelLoadingScreen", false, "", "开启后会禁用加载地形屏幕 理论上能提升一点点加入世界的速度(服务器同理)").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_PLAYER_ARMOR_RENDER = new ConfigBooleanHotkeyed("disablePlayerArmorRender", false, "", "开启此功能后会禁用玩家的盔甲渲染\n终于可以看到涩涩的皮肤啦！").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_PLAYER_LIST_HUD_BACKGROUND = new ConfigBooleanHotkeyed("disableTabBackGround", false, "", "禁用TAB菜单背景").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_CONTAINER_BACKGROUND = new ConfigBooleanHotkeyed("disableContainerBackGround", false, "", "禁用容器背景渲染").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_PACKET_KICK = new ConfigBooleanHotkeyed("disablePacketKick", false, "", "开启后会阻止玩家因为数据包错误被踢出服务器\n此功能建议搭配ViaFabricPLus食用").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed FORCE_JOIN_SERVER_IGNORE_UNKNOWN_PACKET = new ConfigBooleanHotkeyed("disablePacketKick-ForceJoinServer", false, "", "此功能可以无视客户端不存在/未注册的数据包强制进入服务器").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed PREVENT_NET_PRO_ERR = new ConfigBooleanHotkeyed("disableNetworkProError", false, "", "阻止玩家因网络协议错误被踢出服务器").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_PROFILER = new ConfigBooleanHotkeyed("disableProfiler", false, "", "禁用后可提升帧数但会导致饼图不可用").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_SCORE_BOARD_BACK_GROUND = new ConfigBooleanHotkeyed("disableScoreBoardBackGround", false, "", "开启后会禁用计分板背景").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_ITEM_ENTITY_MULPOSE = new ConfigBooleanHotkeyed("disbaleItemEntityMulpose", false, "", "禁用掉落物旋转").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_SUBTITLE_OVERLAY_BACKGROUND = new ConfigBooleanHotkeyed("disableSubtitleBackGround", false, "", "禁用字幕背景").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed CONFIRM_SCREEN_ALWAYS_YES = new ConfigBooleanHotkeyed("disableEnsureScreen", false, "", "禁用确认执行屏幕").apply(ALL);
    }

    public static class Optimizations {
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed OPT_ITEM_FRAME = new ConfigBooleanHotkeyed("optItemFrame", false, "", "开启后面对大量展示框、地图画时可大幅提升平均帧和百分之一low帧\n⚠⚠⚠警告⚠⚠⚠\n由于mojang不同版本间渲染代码改动较大（人话就是我懒得适配）\n仅能保证1.21.11+有满血性能\n测试数据:\nMinecraft 26.2 调优包 572地图画\n关闭优化 平均帧: 370~FPS 百分之一low: 80~FPS\n开启优化 平均帧: 900~FPS 百分之一low: 430~FPS").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigOptionList ITEM_FRAME_VISIBILITY = new ConfigOptionList("optItemFrame-FrameVisibility", ItemFrameVisibility.EMPTY_ONLY, "修改展示框边框的可见性", "").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed OPT_SIGN_TEXT = new ConfigBooleanHotkeyed("optSignText", false, "", "通过优化hasMessage()方法来提升告示牌的渲染性能").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed OPT_ITEM_MODEL = new ConfigBooleanHotkeyed("optItemModel", false, "", "使用Identifier来存储物品模型信息来降低Map查询开销").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed OPT_DIRECTION = new ConfigBooleanHotkeyed("optDirection", false, "", "通过优化Direction#byName方法来提升性能").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed MINI_HUD_FPS_OPT = new ConfigBooleanHotkeyed("optMiniHud", false, "", "MiniHud掉帧优化").apply(ALL);
    }

    public static class LMS {
        @Config(tab = Tab.LMS) public static final ConfigHotkey LMS_TAKE_ITEM = new ConfigHotkey("openFakeFetchGui", "", "打开假人取货菜单").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBoolean LMS_FETCH_SUPPORT = new ConfigBoolean("fakeFetchSupport", false, "需要lms carpet addition").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed MID_CLICK_TAKE_ITEM = new ConfigBooleanHotkeyed("middleClickLitematicBlockFetch", false, "", "启用后鼠标中键点击，原理图内方块(玩家背包内没有)，会立即取货（需要lms carpet addition）\n shift+中键取1盒 \n中键取1组").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed AUTO_OPEN_FAKE_PLAYER_INV = new ConfigBooleanHotkeyed("autoOpenFakeInv", false, "", "自动打开假人背包").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigOptionList AUTO_OPEN_FAKE_PLAYER_INV_MODE = new ConfigOptionList("autoOpenFakeInv-Mode", OpenFakePlayerInvMode.INTERACTION, "交互模式", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed AUTO_KILL_FAKE_PLAYERS = new ConfigBooleanHotkeyed("autoKillFake", false, "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigInteger AUTO_COOLDOWN = new ConfigInteger("autoActionCooldown", 100, 100, 1000, "单位ms", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed FAKE_PLAYER_INVENTORY_ITEM_CACHE = new ConfigBooleanHotkeyed("fakeInvCache", false, "", "开启后会缓存假人背包内的物品 中间投影方块时优先从缓存的假人取出").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigHotkey CLEAN_PLAYER_INV_CHACHE = new ConfigHotkey("fakeInvCache-clear", "", "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed LITEMATICA_CALCULATE_QWP = new ConfigBooleanHotkeyed("MaterialList-CaculateLMS", false, "", "开启后材料投影材料列表会统计全物品").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed LITEMATICA_CALCULATE_FAKE = new ConfigBooleanHotkeyed("MaterialList-CaculateInv", false, "", "开启后材料投影材料列表会统计已缓存的假人背包").apply(ALL);
    }

    public static class Lists {
        @Config(tab = Tab.LISTS) public static final ConfigStringList TAP_FILTER_WHITELIST = new ConfigStringList("tabFilterWhiteList", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList TAP_FILTER_BLACKLIST = new ConfigStringList("tabFilterBlockList", ImmutableList.of(), " ").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList TAP_FILTER_PREFIX = new ConfigStringList("tabFilterPrefix", ImmutableList.of(), " ").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList FAKE_PLAYER_KILL_AURA_BLACKLIST = new ConfigStringList("fakeKillAuraBlackList", ImmutableList.of(), "假人杀戮光环黑名单（仅对名单内玩家生效，精确匹配，忽略大小写）").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList FAKE_PLAYER_KILL_AURA_WHITELIST = new ConfigStringList("killAuraWhiteList", ImmutableList.of(), "假人杀戮光环白名单（仅对名单内玩家生效，精确匹配，忽略大小写）").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList AUTO_WASTE_CLEAN_WHITELIST = new ConfigStringList("inventoryCleanerWhiteList", ImmutableList.of(), "自动垃圾清理白名单").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList AUTO_WASTE_CLEAN_BLACKLIST = new ConfigStringList("inventoryCleanerBlackList", ImmutableList.of(), "自动垃圾清理黑名单").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST = new ConfigStringList("mountLoggerOnMinihudWhitelist", ImmutableList.of(), ".contains(xxx)").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST = new ConfigStringList("mountLoggerOnMinihudBlacklist", ImmutableList.of(), ".contains(xxx)").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList STRONG_BLOCK_COLLISION_WHITELIST = new ConfigStringList("customBlockBlockShapeWhiteList", ImmutableList.of(), "自定义方块碰撞箱白名单").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList STRONG_BLOCK_COLLISION_BLACKLIST = new ConfigStringList("customBlockBlockShapeBlackList", ImmutableList.of(), "自定义方块碰撞箱黑名单").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList STRONG_BLOCK_COLLISION_WHITELIST_SHAPE = new ConfigStringList("customBlockBlockInteractionShape-Whitelist", ImmutableList.of(), "自定义方块交互交互碰撞箱白名单").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList STRONG_BLOCK_COLLISION_BLACKLIST_SHAPE = new ConfigStringList("customBlockBlockInteractionShape-Blacklist", ImmutableList.of(), "自定义方块交互碰撞箱黑名单").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList CUSTOM_LITEMATICA_BLOCK_REPLACE_LIST = new ConfigStringList("customLitematicaBlockReplaceList", ImmutableList.of(), "自定义投影方块替换名单 \n 格式: \n minecraft:item|minecraft:item1").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList AUTO_BOX_RESTROKE_WHITELIST = new ConfigStringList("autoBlockRestrok-Whitelist", ImmutableList.of(), "自动盒子补货白名单").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList AUTO_BOX_RESTROKE_BLACKLIST = new ConfigStringList("autoBlockRestrok-blackList", ImmutableList.of(), "自动盒子补货黑名单").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList FAKE_PLAYER_INVENTORY_ITEM_CACHE_WHITE_LIST = new ConfigStringList("fakeInvCache-Whitelist", ImmutableList.of(), "", "").apply(ALL);
    }

    public static class Commands {
        @Config(tab = Tab.COMMAND) public static final ConfigBooleanHotkeyed PLAYER_MANIPULATE_COMMAND = new ConfigBooleanHotkeyed("commandPm", false, "", "支持批量操作假人 格式 \n /pm <前缀> <开始值> <结束值> <Action> \n /pm <开始值> <结束值> <Action>").apply(ALL);
        @Config(tab = Tab.COMMAND) public static final ConfigInteger PLAYER_MANIPULATE_COMMAND_WAIT_TIME = new ConfigInteger("commandPmExecutionCooldown", 10, 1, 1000, "每执行一个action后的等待时间 单位ms").apply(ALL);
        @Config(tab = Tab.COMMAND) public static final ConfigString PLAYER_MANIPULATE_COMMAND_DEFAULT_PREFIX = new ConfigString("commandPmPrefix", "bot_").apply(ALL);
        @Config(tab = Tab.COMMAND) public static final ConfigBooleanHotkeyed AUTO_VAULT_COMMAND = new ConfigBooleanHotkeyed("commandAutoVault", false, "", "自动开启宝库命令 用法 \n/autovault player <前缀> <开始值> <结束值> <方块X> <方块Y> <方块Z> <DIR> <In> 等效于:\n/player _AntiDeath_ spawn at <方块X> <方块Y> <方块Z> facing <dir> <in>\n/autovault set <宝库XYZ>").apply(ALL);
    }

    public static class Functions {
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed CREEPER_WARN = new ConfigBooleanHotkeyed("creeperWarner", true, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigDouble CREEPER_WARN_RANGE = new ConfigDouble("creeperWarner-Range", 8, 0, 64, "苦力怕预警器范围(以玩家为中心)").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed DISPLAY_REMAIN_ITEM = new ConfigBooleanHotkeyed("remainItemOverlay", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigInteger DISPLAY_REMAIN_ITEM_OVERLAY_Y_OFFSET = new ConfigInteger("remainItemOverlay-yOffset", 0);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigInteger DISPLAY_REMAIN_ITEM_OVERLAY_X_OFFSET = new ConfigInteger("remainItemOverlay-xOffset", 0);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed TAP_FILTER = new ConfigBooleanHotkeyed("tabFilter", false, "", "过滤掉tab菜单无用的玩家/常驻假人").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBoolean ENABLE_TAP_FILTER_WHITELIST = new ConfigBoolean("enableTabFilterWhiteList", false, " ").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBoolean ENABLE_TAP_FILTER_PREFIX = new ConfigBoolean("enableTabFilter-Prefix", false, " ").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed SPECTATOR_TELEPORT_FAKE_PLAYER_LIST = new ConfigBooleanHotkeyed("spectatorTeleportFakePlayerList", false, "", "将延迟为 0 的玩家从普通旁观传送列表分离到假玩家列表").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigHotkey ENABLE_FAKE_PLAYER_KILL_AURA = new ConfigHotkey("triggerFakeKillAura", "", "触发假人杀戮光环").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigString FAKE_PLAYER_KILL_AURA_PREFIX = new ConfigString("fakeKillAuraPrefix", "bot_", "前缀").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigDouble FAKE_PLAYER_KILL_AURA_RANGE = new ConfigDouble("fakeKillAuraRange", 4, 0, 32, "假人杀戮光环范围(以玩家为中心)").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_FAKE_PLAYER_KILL_AURA_BLACKLIST = new ConfigBooleanHotkeyed("enableFakeKillAuraBlackList", false, "", "启用假人杀戮光环黑名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_FAKE_PLAYER_KILL_AURA_WHITELIST = new ConfigBooleanHotkeyed("enableKillAuraWhiteList", false, "", "启用假人杀戮光环白名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed LOW_HEALTH_EXECUTE_OR_SEND = new ConfigBooleanHotkeyed("lowHealthAutoSendMessageOrCmd", false, "", "可自定义命令，当指令不可用时自动发送聊天消息").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigFloat LOW_HEALTH_VALUE = new ConfigFloat("healthValve", 4, 1, 20, "生命值阈值").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigString LOW_HEALTH_SEND_CONTENT_MESSAGE = new ConfigString("SendContent-Message", "!s", "指令").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigString LOW_HEALTH_SEND_CONTENT_COMMAND = new ConfigString("SendContent-Command", "spectator", "消息").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed FORCE_BLOCK_BREAK_COOL_DOWN = new ConfigBooleanHotkeyed("forceBlockMiningCoolDown", false, "", "OMMC移植功能").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed FLAT_MINING = new ConfigBooleanHotkeyed("flatMining", false, "", "OMMC移植功能").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed CAN_ALWAYS_DISCONNECT = new ConfigBooleanHotkeyed("canAlwaysDisconnect", false, "", "为ReconfigScreen和ConnectScreen增加了退出按钮 可随时断开连接").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN = new ConfigBooleanHotkeyed("enableInventoryCleaner", false, "", "开启后可自动清理垃圾").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigHotkey SWITCH_CLEAN_MODE = new ConfigHotkey("inventoryCleanerSwitchMode", "", "切换清理模式").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigOptionList AUTO_WASTE_CLEAN_MODE = new ConfigOptionList("inventoryCleaner-Mode", AutoCleanWasteMode.DROP);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigHotkey SAVE_ITEMS = new ConfigHotkey("inventoryCleaner-SaveInventoryItem", "", " 将玩家背包物品保存至黑名单/白名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN_WHITELIST = new ConfigBooleanHotkeyed("enbaleInventoryCleanerWhiteList", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN_BLACKLIST = new ConfigBooleanHotkeyed("enbaleinventoryCleanerBlackList", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed MOUNT_LOGGERS_ON_MINIHUD = new ConfigBooleanHotkeyed("mountLoggerOnMinihud", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST = new ConfigBooleanHotkeyed("enable-mountLoggerOnMinihudWhitelist", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST = new ConfigBooleanHotkeyed("enable-mountLoggerOnMinihudBlacklist", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed CAN_OPEN_MUTI_PLAYER_SCREEN_ON_GAMING = new ConfigBooleanHotkeyed("canOpenMutilPlayerScreenOnGaming", false, "", "可在游戏中打开多人游戏菜单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION = new ConfigBooleanHotkeyed("enableCustomBlockBlockShape", false, "", "开启后可自定义方块碰撞箱").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_WHITELIST = new ConfigBooleanHotkeyed("enableCustomBlockBlockShapeWhiteList", false, "", "启用自定义方块碰撞箱白名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST = new ConfigBooleanHotkeyed("enableCustomBlockBlockShapeWhiteList", false, "", "启用自定义方块碰撞箱黑名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_SHAPE = new ConfigBooleanHotkeyed("enableCustomBlockBlockInteractionShape", false, "", "开启后可自定义方块交互碰撞箱").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_WHITELIST_SHAPE = new ConfigBooleanHotkeyed("enableCustomBlockBlockInteractionShape-Whtelist", false, "", "启用自定义方块交互碰撞箱白名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST_SHAPE = new ConfigBooleanHotkeyed("enableCustomBlockBlockInteractionShape-Blacklist", false, "", "启用自定义方块交互碰撞箱黑名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed CUSTOM_LITEMATICA_BLOCK_REPLACE = new ConfigBooleanHotkeyed("enableCustomLitematicaBlockReplace", false, "", "启用自定义方块碰撞箱白名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed AUTO_BOX_RESTROKE = new ConfigBooleanHotkeyed("enableAutoBlockRestrok", false, "", "启用自动盒子补货(需要开启tweakeroo的自动补货)").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_BOX_RESTROKE_WHITELIST = new ConfigBooleanHotkeyed("enableAutoBlockRestrok-Whitelist", false, "", "启用自动盒子补货白名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_BOX_RESTROKE_BLACKLIST = new ConfigBooleanHotkeyed("enableAutoBlockRestrok-blackList", false, "", "启用自动盒子补货黑名单").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigHotkey TRIGGER_BOX_SPLITTER = new ConfigHotkey("trigger-boxItemSplitter", "", "按下指定快捷见后将会保存玩家主手的物品并启用潜影盒物品分离器\n会自动打开玩家背包（除快捷栏内)的所有盒子并丢出符合的物品").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed FORCE_USE_FIREWORK = new ConfigBooleanHotkeyed("forceUseFireWork", false, "", "开启后会强制使用烟花火箭").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed USE_SIGN_RUN_COMMAND = new ConfigBooleanHotkeyed("useSignRunCommand", false, "", "当告示牌每一行为 / 开头时 蹲下右键告示牌会自动执行命令").apply(ALL);
    }

    @Config public static final ConfigHotkey ASA = new ConfigHotkey("openGui", "Z,K", "Open Gui Key").apply(ALL);
    @Config public static final ConfigBooleanHotkeyed DEBUG = new ConfigBooleanHotkeyed("debug", false, "", "1111").apply(ALL);
    @Config public static final ConfigHotkey TEST = new ConfigHotkey("触发调试", "", "测试", "1111").apply(ALL);

    private static final String FILE_PATH = "./config/" + AsaMod.MOD_ID + ".json";
    private static final File CONFIG_DIR = new File("./config");
    public static boolean shouldDisableTitle = false;
    public static boolean lockCreativeScreen = false;

    public static void switchMode(ConfigOptionList option) {
        option.setOptionListValue(option.getOptionListValue().cycle(true));
    }

    public static boolean isInList(Object object, List<?> list) {
        return list.contains(object);
    }

    public static boolean isInList(String object, ConfigStringList list) {
        return isInList(object, list.getStrings());
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
                ConfigUtils.readConfigBase(obj, AsaMod.MOD_ID, ConfigsManager.getConfigs(Tab.ALL));
            }
        }
    }

    @Override
    public void save() {
        if ((CONFIG_DIR.exists() && CONFIG_DIR.isDirectory()) || CONFIG_DIR.mkdirs()) {
            JsonObject configRoot = new JsonObject();
            ConfigUtils.writeConfigBase(configRoot, AsaMod.MOD_ID, ConfigsManager.getConfigs(Tab.ALL));
            //? if < 26.1 {
            /*JsonUtils.writeJsonToFile(configRoot, new File(FILE_PATH));
             *///?} else {
            JsonUtils.writeJsonToFile(configRoot, Path.of(FILE_PATH));
            //?}
        }
    }
}
