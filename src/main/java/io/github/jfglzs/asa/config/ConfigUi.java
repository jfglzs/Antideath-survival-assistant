package io.github.jfglzs.asa.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import java.util.List;

import static io.github.jfglzs.asa.AsaMod.*;

public class ConfigUi extends GuiConfigsBase {
    private static Tab tab = Tab.ALL;


    public ConfigUi()
    {
        super(10, 50, MOD_ID, null, "%s V%s 配置界面".formatted(MOD_ID, version));
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;
        for (Tab tab : Tab.values()) {
            x += this.createButton(x, y, -1, tab);
        }
    }

    private int createButton(int x, int y, int width, Tab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getTranslate());
        button.setEnabled(ConfigUi.tab != tab);
        this.addButton(button, new ButtonListener(tab, this));

        return button.getWidth() + 2;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<? extends IConfigBase> configs;
        Tab tab = ConfigUi.tab;
        if (tab == Tab.ALL) {
            configs = Configs.ALL_CONFIGS;
        }
        else if (tab == Tab.LMS) {
            configs = Configs.LMS;
        }
        else if (tab == Tab.DISABLES) {
            configs = Configs.DISABLES;
        }
        else if (tab == Tab.COMMAND) {
            configs = Configs.COMMANDS;
        }
        else {
            configs = Configs.FUNCTIONS;
        }
        return ConfigOptionWrapper.createFor(configs);
    }

    private static class ButtonListener implements IButtonActionListener {
        private final ConfigUi parent;
        private final Tab tab;

        public ButtonListener(Tab tab, ConfigUi parent) {
            this.tab = tab;
            this.parent = parent;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            ConfigUi.tab = this.tab;
            this.parent.reCreateListWidget();
            this.parent.getListWidget().resetScrollbarPosition();
            this.parent.initGui();
        }
    }

    public enum Tab {
        ALL("所有"),
        FUNCTIONS("功能"),
        DISABLES("禁用类功能"),
        COMMAND("命令"),
        LMS("LMS")
        ;

        public final String translation;

        Tab(String translation) {
            this.translation = translation;
        }

        public String getTranslate()
        {
            return StringUtils.translate(translation);
        }
    }

}
