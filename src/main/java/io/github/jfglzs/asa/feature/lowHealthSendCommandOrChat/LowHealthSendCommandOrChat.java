package io.github.jfglzs.asa.feature.lowHealthSendCommandOrChat;

import com.google.common.util.concurrent.RateLimiter;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import io.github.jfglzs.asa.config.options.LowHealthSendMode;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.CommandUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import io.github.jfglzs.asa.config.Configs;

public class LowHealthSendCommandOrChat {
    public static final RateLimiter LIMITER = RateLimiter.create(0.02);

    public static void tick(Minecraft client) {
        Player player = client.player;
        if (Configs.Functions.LOW_HEALTH_EXECUTE_OR_SEND.getBooleanValue() && PlayerUtils.isSurvivalMode(player)) {
            if (player.getHealth() < Configs.Functions.LOW_HEALTH_VALUE.getFloatValue()) {
                if (! LIMITER.tryAcquire())
                    return;
                String cmd = Configs.Functions.LOW_HEALTH_SEND_CONTENT_COMMAND.getStringValue();
                String msg = Configs.Functions.LOW_HEALTH_SEND_CONTENT_MESSAGE.getStringValue();
                IConfigOptionListEntry mode = Configs.Functions.LOW_HEALTH_SEND_CONTENT_MODE.getOptionListValue();
                if (mode == LowHealthSendMode.AUTO) {
                    if (CommandUtils.canUseCommand(cmd))
                        MCUtils.executeCommand(cmd);
                    else
                        ChatUtils.serverMess(msg);
                }
                else if (mode == LowHealthSendMode.SEND_CHAT_MESSAGE) {
                    ChatUtils.serverMess(msg);
                }
                else {
                    MCUtils.executeCommand(cmd);
                }
            }
        }
    }
}
