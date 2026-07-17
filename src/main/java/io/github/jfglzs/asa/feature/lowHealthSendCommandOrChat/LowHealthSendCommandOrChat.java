package io.github.jfglzs.asa.feature.lowHealthSendCommandOrChat;

import com.google.common.util.concurrent.RateLimiter;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import static io.github.jfglzs.asa.config.Configs.*;

public class LowHealthSendCommandOrChat {
    public static final RateLimiter rateLimiter = RateLimiter.create(0.05);

    public static void trigger(Minecraft client) {
        Player player = client.player;
        if (LOW_HEALTH_EXECUTE_OR_SEND.getBooleanValue() && PlayerUtils.isSurvivalMode(player)) {
            float health = player.getHealth();
            if (health < LOW_HEALTH_VALUE.getFloatValue()) {
                if (!rateLimiter.tryAcquire()) return;

                String content = LOW_HEALTH_SEND_CONTENT.getStringValue();
                if (LOW_HEALTH_SEND_MODE.getOptionListValue().getStringValue().equals("发送聊天消息")) {
                    ChatUtils.sendMessageToServer(content);
                    AsaMod.debugMessage("Send Chat %s".formatted(content));
                }
                else {
                    MCUtils.executeCommand(content);
                    AsaMod.debugMessage("Send Command %s".formatted(content));
                }
            }
        }
    }
}
