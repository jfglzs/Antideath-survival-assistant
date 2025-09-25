package io.github.jfglzs.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenu implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return (screen) ->
        {
            ConfigUi ui = new ConfigUi();
            ui.setParent(screen);
            return ui;
        };
    }
}
