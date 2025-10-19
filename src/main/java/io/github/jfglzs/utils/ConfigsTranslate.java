package io.github.jfglzs.utils;

import fi.dy.masa.malilib.util.StringUtils;
import io.github.jfglzs.config.Configs;

public class ConfigsTranslate
{
    //先凑合着用 懒得在研究翻译了 就暴力一点吧(

    public static void translate()
    {
        for (int i = 0; i <= Configs.ALL.size() -1; i++)
        {
            Configs.ALL.get(i).setComment(ConfigsTranslateEnum.values()[i].translatedComment);
            Configs.ALL.get(i).setTranslatedName(ConfigsTranslateEnum.values()[i].translated);
        }
    }

    public enum ConfigsTranslateEnum
    {
        ASA("asa.config.opengui"),
        CREEPER_WARN("asa.config.creeperwarn"),
        CREEPER_WARN_RANGE("asa.config.creeperwarn.range"),
        MATERIAL_RECYCLER("asa.config.material_recycler"),
        MATERIAL_RECYCLER_LIST("asa.config.material_recycler_list"),
        ENABLE_MATERIAL_RECYCLER_BLACK_LIST("asa.config.enable_material_recycler_black_list"),
        MATERIAL_RECYCLER_BLACK_LIST("asa.config.material_recycler_black_list"),

        DISABLE_LOADING_TERRAIN_SCREEN("asa.config.disable_loading_terrain_screen"),
        DISABLE_SUBTITLE("asa.config.disable_subtitle"),
        DISABLE_CONNECT_TIMED_OUT("asa.config.disable_connect_timed_out"),
        DISABLE_PLAYER_ARMOR_RENDER("asa.config.disable_player_armor_render"),
        DISABLE_PLACE_BLOCK_NEARBY_PORTAL("asa.config.disable_place_block_nearby_portal"),
        DISABLE_PLACE_BLOCK_NEARBY_PORTAL_WHITELIST("asa.config.disable_place_block_nearby_portal_whitelist"),

        MATERIAL_RECYCLER_AUTO("asa.config.material_recycler_auto"),
        DISPLAY_REMAIN_ITEM("asa.config.display_remain_item"),

        SWITCH_SERVER("asa.config.switch_server"),
        SWITCH_SERVER_COMMAND("asa.config.switch_server_command"),

        SWITCH_SERVER_SINGLE_1("asa.config.switch_server_single_1"),
        SWITCH_SERVER_SINGLE_1S("asa.config.switch_server_single_1s"),
        SWITCH_SERVER_SINGLE_2("asa.config.switch_server_single_2"),
        SWITCH_SERVER_SINGLE_2S("asa.config.switch_server_single_2s"),
        SWITCH_SERVER_SINGLE_3("asa.config.switch_server_single_3"),
        SWITCH_SERVER_SINGLE_3S("asa.config.switch_server_single_3s"),

        SWITCH_ITEM_LIST("asa.config.switch_item_list"),
        SWITCH_ITEM("asa.config.switch_item"),
        TAP_FILTER("asa.config.tap_filter"),
        TAP_FILTER_WHITELIST("asa.config.tap_filter_whitelist"),

        TEST("asa.config.test")
        ;

        public final String translated;
        public final String translatedComment;

        ConfigsTranslateEnum(String translateKey)
        {
            this.translated = StringUtils.translate(translateKey);
            this.translatedComment = StringUtils.translate(translateKey + ".comment");
        }
    }
}