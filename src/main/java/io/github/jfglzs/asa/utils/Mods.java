package io.github.jfglzs.asa.utils;

public class Mods {
    public static final boolean quickshulker;
    public static final boolean item_scroller;
    public static final boolean tweakeroo;
    public static final boolean lithium;

    static {
        quickshulker = MCUtils.isModLoaded("quickshulker");
        item_scroller = MCUtils.isModLoaded("itemscroller");
        tweakeroo = MCUtils.isModLoaded("tweakeroo");
        lithium = MCUtils.isModLoaded("lithium");
    }
}
