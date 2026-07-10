package io.github.jfglzs.asa.utils;

public class Mods {
    public static boolean isShulkerBoxLoaded = false;

    public static void init() {
        isShulkerBoxLoaded = MCUtils.isModLoaded("quickshulker");
    }
}
