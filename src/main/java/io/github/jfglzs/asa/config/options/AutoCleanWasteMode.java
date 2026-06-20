package io.github.jfglzs.asa.config.options;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;

public enum AutoCleanWasteMode implements IConfigOptionListEntry {
    DROP("丢出物品"),
    MOVE_TO_CONTAINER("转移至容器");

    private final String name;

    AutoCleanWasteMode(String name) {
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
            if (++id >= values().length) {
                id = 0;
            }
        }
        else {
            if (--id < 0) {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public IConfigOptionListEntry fromString(String value) {
        for (AutoCleanWasteMode mode : values()) {
            if (mode.name.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return DROP;
    }
}
