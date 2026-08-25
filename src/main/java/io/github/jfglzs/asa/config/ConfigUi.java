package io.github.jfglzs.asa.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
//~ if >= 26.1 '.JsonUtils' -> '.data.json.JsonUtils' {
import fi.dy.masa.malilib.util.data.json.JsonUtils;
//~}

import static io.github.jfglzs.asa.AsaMod.*;

public class ConfigUi extends GuiConfigsBase implements IConfigHandler {
    private static Tab tab = Tab.ALL;
    private static final String FILE_PATH = "./config/" + MOD_ID + ".json";
    private static final File CONFIG_DIR = new File("./config");
    public static final ConfigUi INSTANCE = new ConfigUi();

    public ConfigUi() {
        super(10, 50, MOD_ID_FANCY, null, "%s V%s 配置界面".formatted(MOD_ID, version));
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;
        for (Tab tab : Tab.values()) {
            x += this.createButton(x, y, - 1, tab);
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
        configs = ConfigsManager.getConfigs(ConfigUi.tab);
        return ConfigOptionWrapper.createFor(configs);
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

    @Override
    protected void closeGui(boolean showParent) {
        INSTANCE.save();
        super.closeGui(showParent);
    }
}
