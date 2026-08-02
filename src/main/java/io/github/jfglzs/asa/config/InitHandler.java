package io.github.jfglzs.asa.config;

import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import io.github.jfglzs.asa.AsaMod;

public class InitHandler implements IInitializationHandler {
    public static final InitHandler INSTANCE = new InitHandler();
    @Override
    public void registerModHandlers() {
        Registry.CONFIG_SCREEN.registerConfigScreenFactory(
                new ModInfo(AsaMod.MOD_ID, AsaMod.MOD_ID_FANCY, ConfigUi::new)
        );
    }
}
