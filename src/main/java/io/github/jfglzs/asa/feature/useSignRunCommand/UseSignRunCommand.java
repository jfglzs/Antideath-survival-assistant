package io.github.jfglzs.asa.feature.useSignRunCommand;

import com.google.common.util.concurrent.RateLimiter;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.events.SendPacketEvent;
import io.github.jfglzs.asa.feature.disablePacketKick.ASAFakePacket;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class UseSignRunCommand {
    private static RateLimiter limiter = RateLimiter.create(1);

    public static void init() {
        SendPacketEvent.INSTANCE.register(packet -> {
            if (! limiter.tryAcquire()) return packet;

            if (packet instanceof ServerboundUseItemOnPacket useItemPacket && Configs.USE_SIGN_RUN_COMMAND.getBooleanValue()) {
                BlockHitResult hitResult = useItemPacket.getHitResult();
                LocalPlayer player = MCUtils.getLocalPlayer();
                if (hitResult.getType() == HitResult.Type.BLOCK && player.isShiftKeyDown()) {
                    BlockPos pos = hitResult.getBlockPos();
                    ClientLevel level = MCUtils.getLevel();
                    BlockEntity entity = level.getBlockEntity(pos);
                    if (entity instanceof SignBlockEntity sign) {
                        Component[] messages = sign.getFrontText().getMessages(false);
                        for (Component component : messages) {
                            String command = component.getString();
                            if (command.startsWith("/")) {
                                MCUtils.executeCommand(command);
                            }
                        }
                        return ASAFakePacket.INSTANCE;
                    }
                }
            }
            return packet;
        });
    }
}
