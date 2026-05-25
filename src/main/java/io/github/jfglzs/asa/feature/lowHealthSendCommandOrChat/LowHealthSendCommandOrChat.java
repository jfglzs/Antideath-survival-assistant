package io.github.jfglzs.asa.feature.lowHealthSendCommandOrChat;

import com.google.common.util.concurrent.RateLimiter;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.Minecraft;

import static io.github.jfglzs.asa.config.Configs.*;

public class LowHealthSendCommandOrChat {
    public static final RateLimiter rateLimiter = RateLimiter.create(0.1);

    public static void trigger(Minecraft client) {
        if (LOW_HEALTH_EXECUTE_OR_SEND.getBooleanValue() && client.player != null && client.player.getHealth() < LOW_HEALTH_VALUE.getFloatValue()) {
            if (client.player.isSpectator() || client.player.isCreative() || !rateLimiter.tryAcquire()) return;

            if (LOW_HEALTH_SEND_MODE.getOptionListValue().getStringValue().equals("发送聊天消息")) {
                MCUtils.ChatUtils.sendMessageToServer(Configs.LOW_HEALTH_SEND_CONTENT.getStringValue());
            }
            else {
                MCUtils.excuteCommand(Configs.LOW_HEALTH_SEND_CONTENT.getStringValue());
            }
        }
    }
}
