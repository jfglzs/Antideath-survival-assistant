package io.github.jfglzs.asa.mixin.masa.minihud;


import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.minihud.event.RenderHandler;
import io.github.jfglzs.asa.accessor.IClientPacketListener;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = RenderHandler.class, priority = 1900)
public class RenderHandler_Mixin {
    @Shadow
    @Final
    private List<String> lines;
    @Unique
    private final List<String> asa$list = new ObjectArrayList<>(32);


    @ModifyReceiver(
            method = "updateLines",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1),
            remap = false
    )
    private List<String> updateLines_add(List<String> original, Object e) {
        return Configs.MINI_HUD_FPS_OPT.getBooleanValue() ? asa$list : original;
    }

    @ModifyReceiver(
            method = "updateLines",
            at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 1),
            remap = false
    )
    private List<String> updateLines_clear(List<String> original) {
        return Configs.MINI_HUD_FPS_OPT.getBooleanValue() ? asa$list : original;
    }

    @WrapOperation(
            //? if >= 26.1 {
            method = "onExtractGuiOverlayPost",
            //?} else if > 1.21.1 {
            /*method = "onRenderGameOverlayPostAdvanced",
            *///?} else {
            /*method = "onRenderGameOverlayPost",
            *///?}
            at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/event/RenderHandler;updateLines()V"),
            remap = false
    )
    private void onRenderGameOverlayPostAdvanced(RenderHandler instance, Operation<Void> original) {
        if (Configs.MINI_HUD_FPS_OPT.getBooleanValue()) {
            ThreadUtils.runOnTaskThread(() -> {
                original.call(instance);
                asa$mountHudInfo();
                ThreadUtils.runOnClientThread(() -> {
                    lines.clear();
                    lines.addAll(asa$list);
                });
            });
            return;
        }
        original.call(instance);
        asa$mountHudInfo();
    }

    @Unique
    private void asa$mountHudInfo() {
        if (! Configs.MOUNT_LOGGERS_ON_MINIHUD.getBooleanValue()) return;

        Minecraft mc = MCUtils.getMinecraft();
        LocalPlayer player = mc.player;
        List<String> list = Configs.MINI_HUD_FPS_OPT.getBooleanValue() ? asa$list : lines;

        if (player != null && player.connection instanceof IClientPacketListener listener) {
            List<String> tabList = listener.asa$TabList();
            if (tabList == null) return;

            for (String line : tabList) {
                if (Configs.ENABLE_MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST.getBooleanValue()) {
                    if (Configs.MOUNT_LOGGERS_ON_MINIHUD_WHITE_LIST.getStrings().stream().anyMatch(line::contains)) {
                        list.add(line);
                    }
                    continue;
                }
                else if (Configs.ENABLE_MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST.getBooleanValue()) {
                    if (Configs.MOUNT_LOGGERS_ON_MINIHUD_BLACK_LIST.getStrings().stream().noneMatch(line::contains)) {
                        list.add(line);
                    }
                    continue;
                }
                list.add(line);
            }
        }
    }
}
