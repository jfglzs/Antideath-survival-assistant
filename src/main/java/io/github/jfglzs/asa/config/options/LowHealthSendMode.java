package io.github.jfglzs.asa.config.options;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum LowHealthSendMode implements IConfigOptionListEntry {
    SEND_CHAT_MESSAGE("发送聊天消息", "asa.opts.lhsm.chat"),
    SEND_COMMAND("发送命令", "asa.opts.lhsm.cmd"),
    AUTO("自动", "asa.opts.lhsm.auto");

    private final String name;
    private final String key;

    LowHealthSendMode(String name, String key) {
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
        for (LowHealthSendMode mode : values()) {
            if (mode.name.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return SEND_CHAT_MESSAGE;
    }
}
