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
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_SUBTITLE = new ConfigBooleanHotkeyed("disableSubtitle", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_CONNECT_TIMED_OUT = new ConfigBooleanHotkeyed("disableConnectionTimedOut", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_LOADING_TERRAIN_SCREEN = new ConfigBooleanHotkeyed("disableLevelLoadingScreen", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_PLAYER_ARMOR_RENDER = new ConfigBooleanHotkeyed("disablePlayerArmorRender", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_PLAYER_LIST_HUD_BACKGROUND = new ConfigBooleanHotkeyed("disableTabBackGround", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_CONTAINER_BACKGROUND = new ConfigBooleanHotkeyed("disableContainerBackGround", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_PACKET_KICK = new ConfigBooleanHotkeyed("disablePacketKick", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed FORCE_JOIN_SERVER_IGNORE_UNKNOWN_PACKET = new ConfigBooleanHotkeyed("disablePacketKick-ForceJoinServer", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed PREVENT_NET_PRO_ERR = new ConfigBooleanHotkeyed("disableNetworkProError", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_PROFILER = new ConfigBooleanHotkeyed("disableProfiler", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_SCORE_BOARD_BACK_GROUND = new ConfigBooleanHotkeyed("disableScoreBoardBackGround", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_ITEM_ENTITY_MULPOSE = new ConfigBooleanHotkeyed("disbaleItemEntityMulpose", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed DISABLE_SUBTITLE_OVERLAY_BACKGROUND = new ConfigBooleanHotkeyed("disableSubtitleBackGround", false, "", "").apply(ALL);
        @Config(tab = Tab.DISABLES) public static final ConfigBooleanHotkeyed CONFIRM_SCREEN_ALWAYS_YES = new ConfigBooleanHotkeyed("disableEnsureScreen", false, "", "").apply(ALL);
    }

    public static class Optimizations {
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed OPT_ITEM_FRAME = new ConfigBooleanHotkeyed("optItemFrame", false, "", "").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigOptionList ITEM_FRAME_VISIBILITY = new ConfigOptionList("optItemFrame-FrameVisibility", ItemFrameVisibility.EMPTY_ONLY, "", "").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed OPT_SIGN_TEXT = new ConfigBooleanHotkeyed("optSignText", false, "", "").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed OPT_ITEM_MODEL = new ConfigBooleanHotkeyed("optItemModel", false, "", "").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed OPT_DIRECTION = new ConfigBooleanHotkeyed("optDirection", false, "", "").apply(ALL);
        @Config(tab = Tab.OPTIMIZATIONS) public static final ConfigBooleanHotkeyed MINI_HUD_FPS_OPT = new ConfigBooleanHotkeyed("optMiniHud", false, "", "").apply(ALL);
    }

    public static class LMS {
        @Config(tab = Tab.LMS) public static final ConfigHotkey LMS_TAKE_ITEM = new ConfigHotkey("openFakeFetchGui", "", "打开假人取货菜单").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBoolean LMS_FETCH_SUPPORT = new ConfigBoolean("fakeFetchSupport", false, "需要lms carpet addition").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed MID_CLICK_TAKE_ITEM = new ConfigBooleanHotkeyed("middleClickLitematicBlockFetch", false, "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed AUTO_OPEN_FAKE_PLAYER_INV = new ConfigBooleanHotkeyed("autoOpenFakeInv", false, "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigOptionList AUTO_OPEN_FAKE_PLAYER_INV_MODE = new ConfigOptionList("autoOpenFakeInv-Mode", OpenFakePlayerInvMode.INTERACTION, "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed AUTO_KILL_FAKE_PLAYERS = new ConfigBooleanHotkeyed("autoKillFake", false, "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigInteger AUTO_COOLDOWN = new ConfigInteger("autoActionCooldown", 100, 100, 1000, "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed FAKE_PLAYER_INVENTORY_ITEM_CACHE = new ConfigBooleanHotkeyed("fakeInvCache", false, "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigHotkey CLEAN_PLAYER_INV_CHACHE = new ConfigHotkey("fakeInvCache-clear", "", "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed LITEMATICA_CALCULATE_QWP = new ConfigBooleanHotkeyed("MaterialList-CaculateLMS", false, "", "").apply(ALL);
        @Config(tab = Tab.LMS) public static final ConfigBooleanHotkeyed LITEMATICA_CALCULATE_FAKE = new ConfigBooleanHotkeyed("MaterialList-CaculateInv", false, "", "").apply(ALL);
    }

    public static class Lists {
        @Config(tab = Tab.LISTS) public static final ConfigStringList TAP_FILTER_WHITELIST = new ConfigStringList("tabFilterWhiteList", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList TAP_FILTER_BLACKLIST = new ConfigStringList("tabFilterBlockList", ImmutableList.of(), " ").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList TAP_FILTER_PREFIX = new ConfigStringList("tabFilterPrefix", ImmutableList.of(), " ").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList FAKE_PLAYER_KILL_AURA_BLACKLIST = new ConfigStringList("fakeKillAuraBlackList", ImmutableList.of(), "（仅对名单内玩家生效，精确匹配，忽略大小写）").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList FAKE_PLAYER_KILL_AURA_WHITELIST = new ConfigStringList("killAuraWhiteList", ImmutableList.of(), "（仅对名单内玩家生效，精确匹配，忽略大小写）").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList AUTO_WASTE_CLEAN_WHITELIST = new ConfigStringList("inventoryCleanerWhiteList", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList AUTO_WASTE_CLEAN_BLACKLIST = new ConfigStringList("inventoryCleanerBlackList", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST = new ConfigStringList("mountLoggerOnMinihudWhitelist", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST = new ConfigStringList("mountLoggerOnMinihudBlacklist", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList STRONG_BLOCK_COLLISION_WHITELIST = new ConfigStringList("customBlockBlockShapeWhiteList", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList STRONG_BLOCK_COLLISION_BLACKLIST = new ConfigStringList("customBlockBlockShapeBlackList", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList STRONG_BLOCK_COLLISION_WHITELIST_SHAPE = new ConfigStringList("customBlockBlockInteractionShape-Whitelist", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList STRONG_BLOCK_COLLISION_BLACKLIST_SHAPE = new ConfigStringList("customBlockBlockInteractionShape-Blacklist", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList CUSTOM_LITEMATICA_BLOCK_REPLACE_LIST = new ConfigStringList("customLitematicaBlockReplaceList", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList AUTO_BOX_RESTROKE_WHITELIST = new ConfigStringList("autoBlockRestrok-Whitelist", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList AUTO_BOX_RESTROKE_BLACKLIST = new ConfigStringList("autoBlockRestrok-blackList", ImmutableList.of(), "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList FAKE_PLAYER_INVENTORY_ITEM_CACHE_WHITE_LIST = new ConfigStringList("fakeInvCache-Whitelist", ImmutableList.of(), "", "").apply(ALL);
        @Config(tab = Tab.LISTS) public static final ConfigStringList SERVER_COMMAND_MAPPING_LIST = new ConfigStringList("serverCommandMapping", ImmutableList.of(), "", "").apply(ALL);
    }

    public static class Commands {
        @Config(tab = Tab.COMMAND) public static final ConfigBooleanHotkeyed PLAYER_MANIPULATE_COMMAND = new ConfigBooleanHotkeyed("commandPm", false, "", "").apply(ALL);
        @Config(tab = Tab.COMMAND) public static final ConfigInteger PLAYER_MANIPULATE_COMMAND_WAIT_TIME = new ConfigInteger("commandPmExecutionCooldown", 10, 1, 1000, "").apply(ALL);
        @Config(tab = Tab.COMMAND) public static final ConfigString PLAYER_MANIPULATE_COMMAND_DEFAULT_PREFIX = new ConfigString("commandPmPrefix", "bot_").apply(ALL);
        @Config(tab = Tab.COMMAND) public static final ConfigBooleanHotkeyed AUTO_VAULT_COMMAND = new ConfigBooleanHotkeyed("commandAutoVault", false, "", "").apply(ALL);
    }

    public static class Functions {
        @Config(tab = Tab.FUNCTIONS) public static final ConfigHotkey ASA = new ConfigHotkey("openGui", "Z,K", "Open Gui Key").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed CREEPER_WARN = new ConfigBooleanHotkeyed("creeperWarner", true, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigDouble CREEPER_WARN_RANGE = new ConfigDouble("creeperWarner-Range", 8, 0, 64, "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed DISPLAY_REMAIN_ITEM = new ConfigBooleanHotkeyed("remainItemOverlay", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigInteger DISPLAY_REMAIN_ITEM_OVERLAY_Y_OFFSET = new ConfigInteger("remainItemOverlay-yOffset", 0);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigInteger DISPLAY_REMAIN_ITEM_OVERLAY_X_OFFSET = new ConfigInteger("remainItemOverlay-xOffset", 0);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed TAP_FILTER = new ConfigBooleanHotkeyed("tabFilter", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBoolean ENABLE_TAP_FILTER_WHITELIST = new ConfigBoolean("enableTabFilterWhiteList", false, " ").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBoolean ENABLE_TAP_FILTER_PREFIX = new ConfigBoolean("enableTabFilter-Prefix", false, " ").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigHotkey ENABLE_FAKE_PLAYER_KILL_AURA = new ConfigHotkey("triggerFakeKillAura", "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigString FAKE_PLAYER_KILL_AURA_PREFIX = new ConfigString("fakeKillAuraPrefix", "bot_", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigDouble FAKE_PLAYER_KILL_AURA_RANGE = new ConfigDouble("fakeKillAuraRange", 4, 0, 32, "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_FAKE_PLAYER_KILL_AURA_BLACKLIST = new ConfigBooleanHotkeyed("enableFakeKillAuraBlackList", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_FAKE_PLAYER_KILL_AURA_WHITELIST = new ConfigBooleanHotkeyed("enableKillAuraWhiteList", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed LOW_HEALTH_EXECUTE_OR_SEND = new ConfigBooleanHotkeyed("lowHealthAutoSendMessageOrCmd", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigFloat LOW_HEALTH_VALUE = new ConfigFloat("healthValve", 4, 1, 20, "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigString LOW_HEALTH_SEND_CONTENT_MESSAGE = new ConfigString("SendContent-Message", "!s", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigString LOW_HEALTH_SEND_CONTENT_COMMAND = new ConfigString("SendContent-Command", "spectator", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed FORCE_BLOCK_BREAK_COOL_DOWN = new ConfigBooleanHotkeyed("forceBlockMiningCoolDown", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed FLAT_MINING = new ConfigBooleanHotkeyed("flatMining", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed CAN_ALWAYS_DISCONNECT = new ConfigBooleanHotkeyed("canAlwaysDisconnect", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN = new ConfigBooleanHotkeyed("enableInventoryCleaner", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigHotkey SWITCH_CLEAN_MODE = new ConfigHotkey("inventoryCleanerSwitchMode", "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigOptionList AUTO_WASTE_CLEAN_MODE = new ConfigOptionList("inventoryCleanerMode", AutoCleanWasteMode.DROP);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigHotkey SAVE_ITEMS = new ConfigHotkey("inventoryCleaner-SaveInventoryItem", "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN_WHITELIST = new ConfigBooleanHotkeyed("enbaleInventoryCleanerWhiteList", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_WASTE_CLEAN_BLACKLIST = new ConfigBooleanHotkeyed("enbaleinventoryCleanerBlackList", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed MOUNT_LOGGERS_ON_MINIHUD = new ConfigBooleanHotkeyed("mountLoggerOnMinihud", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST = new ConfigBooleanHotkeyed("enable-mountLoggerOnMinihudWhitelist", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST = new ConfigBooleanHotkeyed("enable-mountLoggerOnMinihudBlacklist", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed CAN_OPEN_MUTI_PLAYER_SCREEN_ON_GAMING = new ConfigBooleanHotkeyed("canOpenMutilPlayerScreenOnGaming", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION = new ConfigBooleanHotkeyed("enableCustomBlockBlockShape", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_WHITELIST = new ConfigBooleanHotkeyed("enableCustomBlockBlockShapeWhiteList", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST = new ConfigBooleanHotkeyed("enableCustomBlockBlockShapeWhiteList", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_SHAPE = new ConfigBooleanHotkeyed("enableCustomBlockBlockInteractionShape", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_WHITELIST_SHAPE = new ConfigBooleanHotkeyed("enableCustomBlockBlockInteractionShape-Whtelist", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST_SHAPE = new ConfigBooleanHotkeyed("enableCustomBlockBlockInteractionShape-Blacklist", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed CUSTOM_LITEMATICA_BLOCK_REPLACE = new ConfigBooleanHotkeyed("enableCustomLitematicaBlockReplace", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed AUTO_BOX_RESTROKE = new ConfigBooleanHotkeyed("enableAutoBlockRestrok", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_BOX_RESTROKE_WHITELIST = new ConfigBooleanHotkeyed("enableAutoBlockRestrok-Whitelist", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_AUTO_BOX_RESTROKE_BLACKLIST = new ConfigBooleanHotkeyed("enableAutoBlockRestrok-blackList", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigHotkey TRIGGER_BOX_SPLITTER = new ConfigHotkey("trigger-boxItemSplitter", "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed FORCE_USE_FIREWORK = new ConfigBooleanHotkeyed("forceUseFireWork", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed USE_SIGN_RUN_COMMAND = new ConfigBooleanHotkeyed("useSignRunCommand", false, "", "").apply(ALL);
        @Config(tab = Tab.FUNCTIONS) public static final ConfigBooleanHotkeyed ENABLE_SERVER_COMMAND_MAPPING = new ConfigBooleanHotkeyed("enableServerCommandMapping", false, "", "").apply(ALL);

    }

    @Config public static final ConfigBooleanHotkeyed DEBUG = new ConfigBooleanHotkeyed("debug", false, "", "").apply(ALL);
    @Config public static final ConfigHotkey TEST = new ConfigHotkey("触发调试", "", "", "").apply(ALL);

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
