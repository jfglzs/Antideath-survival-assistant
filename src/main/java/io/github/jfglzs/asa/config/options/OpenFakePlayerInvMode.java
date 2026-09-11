package io.github.jfglzs.asa.config.options;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum OpenFakePlayerInvMode implements IConfigOptionListEntry {
    COMMAND("指令交互", "asa.opts.ofpm.cmd"),
    INTERACTION("右键交互", "asa.opts.ofpm.use");

    private final String name;
    private String key;

    OpenFakePlayerInvMode(String name, String key) {
        this.name = name;
        this.key = key;
    }

    @Override
    public String getStringValue() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return StringUtils.translate(this.key);
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
