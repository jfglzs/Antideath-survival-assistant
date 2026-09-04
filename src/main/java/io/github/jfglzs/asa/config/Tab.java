package io.github.jfglzs.asa.config;

import fi.dy.masa.malilib.util.StringUtils;

public enum Tab {
    ALL("asa.config.cate.all"),
    FUNCTIONS("asa.config.cate.functions"),
    DISABLES("asa.config.cate.disables"),
    COMMAND("asa.config.cate.cmd"),
    OPTIMIZATIONS("asa.config.cate.opt"),
    LISTS("asa.config.cate.lists"),
    LMS("asa.config.cate.lms"),
    ORGANIZE("asa.config.cate.organize");

    public final String translation;

    Tab(String translation) {
        this.translation = translation;
    }

    public String getTranslate() {
        return StringUtils.translate(translation);
    }
}
