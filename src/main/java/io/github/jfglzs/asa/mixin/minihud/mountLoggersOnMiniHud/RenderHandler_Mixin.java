package io.github.jfglzs.asa.mixin.minihud.mountLoggersOnMiniHud;

import fi.dy.masa.minihud.event.RenderHandler;
import io.github.jfglzs.asa.accessor.IClientPacketListener;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RenderHandler.class)
public class RenderHandler_Mixin {
    @Shadow
    @Final
    private List<String> lines;

    @Inject(
            method = "updateLines",
            at = @At(value = "TAIL")
    )
    private void updateLines(CallbackInfo ci) {
        if (!Configs.MOUNT_LOGGERS_ON_MINIHUD.getBooleanValue()) return;

        Minecraft mc = MCUtils.getMinecraftClient();
        LocalPlayer player = mc.player;

        if (mc.player != null && player.connection instanceof IClientPacketListener listener) {
            List<String> tabList = listener.asa$TabList();
            if (tabList != null) {
                lines.add("Carpet-Loggers:");
                for (String line : tabList) {
                    if (Configs.ENABLE_MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST.getBooleanValue()) {
                        if (Configs.MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST.getStrings().stream().anyMatch(line::contains)) {
                            lines.add(line);
                        }
                        continue;
                    }
                    else if (Configs.ENABLE_MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST.getBooleanValue()) {
                        if (Configs.MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST.getStrings().stream().noneMatch(line::contains)) {
                            lines.add(line);
                        }
                        continue;
                    }
                    lines.add(line);
                }
            }
        }
    }
}
