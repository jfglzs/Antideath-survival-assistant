package io.github.jfglzs.asa.utils;

import net.minecraft.network.chat.Component;

public class ProgressBar {
    public static Component getProgress(double progress) {
        return getProgress(progress, null);
    }

    public static Component getProgress(double progress, String progressText) {
        int length = 10;
        progress = Math.clamp(progress, 0.0, 1.0);
        int filled = (int) Math.round(progress * length);

        StringBuilder builder = new StringBuilder(length + 8);

        if (progressText != null) builder.append(progressText);

        builder.repeat("█", Math.max(0, filled));
        builder.repeat("-", Math.max(0, length - filled));
        builder.append(' ').append((int) (progress * 100)).append('%');

        return ChatUtils.toComponent(builder.toString());
    }
}
