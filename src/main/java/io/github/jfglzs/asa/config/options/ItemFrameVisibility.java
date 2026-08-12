package io.github.jfglzs.asa.config.options;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;

public enum ItemFrameVisibility implements IConfigOptionListEntry {
    ALWAYS_VISIBLE("永远可见(低性能)"),
    EMPTY_ONLY("无物品时可见(平衡)"),
    ALWAYS_INVISIBLE("永不可见(高性能)");

    private final String name;

    ItemFrameVisibility(String name) {
        this.name = name;
    }

    @Override
    public String getStringValue() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        int id = this.ordinal();

        if (forward) {
            if (++ id >= values().length) {
                id = 0;
            }
        }
        else {
            if (-- id < 0) {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public IConfigOptionListEntry fromString(String value) {
        for (ItemFrameVisibility mode : values()) {
            if (mode.name.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return EMPTY_ONLY;
    }
}
