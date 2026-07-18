package io.github.jfglzs.asa.config;

import fi.dy.masa.malilib.util.StringUtils;

public enum Tab {
    ALL("所有"),
    FUNCTIONS("功能"),
    DISABLES("禁用类功能"),
    COMMAND("命令"),
    OPTIMIZATIONS("优化"),
    LISTS("列表"),
    LMS("假人全物品");

    public final String translation;

    Tab(String translation) {
        this.translation = translation;
    }

    public String getTranslate() {
        return StringUtils.translate(translation);
    }
}
