package io.github.jfglzs.asa.utils;

public class Mods {
    public static boolean quickshulker = false;
    public static boolean item_scroller = false;
    public static boolean lithium = false;

    public static void init() {
        quickshulker = MCUtils.isModLoaded("quickshulker");
        item_scroller = MCUtils.isModLoaded("itemscroller");
        lithium = MCUtils.isModLoaded("lithium");
    }
}
