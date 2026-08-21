package io.github.jfglzs.asa.config.options;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;

public enum OpenFakePlayerInvMode implements IConfigOptionListEntry {
    COMMAND("指令交互"),
    INTERACTION("右键交互");

    private final String name;

    OpenFakePlayerInvMode(String name) {
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
        for (OpenFakePlayerInvMode mode : values()) {
            if (mode.name.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return INTERACTION;
    }
}
