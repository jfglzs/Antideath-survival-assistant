package io.github.jfglzs.utils;

import net.fabricmc.loader.api.FabricLoader;

public class FabricUtils {
    public static boolean isModLoaded(String modId)
    {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

}
