package io.github.jfglzs.asa.mixin.minihud;


import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.minihud.event.RenderHandler;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ThreadUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderHandler.class)
public class RenderHandler_Mixin {
    @Shadow
    @Final
    private List<String> lines;
    @Unique
    private final List<String> list = new ArrayList<>();


    @ModifyReceiver(
            method = "updateLines",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1)
    )
    private List<String> updateLines_add(List<String> original, Object e) {
        if (Configs.MINI_HUD_FPS_OPT.getBooleanValue()) {
            return list;
        }
        return original;
    }

    @ModifyReceiver(
            method = "updateLines",
            at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 1)
    )
    private List<String> updateLines_clear(List<String> original) {
        if (Configs.MINI_HUD_FPS_OPT.getBooleanValue()) {
            return list;
        }
        return original;
    }

    @WrapOperation(
            //~ if > 1.21.1 'onRenderGameOverlayPost' -> 'onRenderGameOverlayPostAdvanced' {
                //~ if >= 26.1 'onRenderGameOverlayPostAdvanced' -> 'onExtractGuiOverlayPost'{
            method = "onExtractGuiOverlayPost",
                //~}
            //~}
            at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/event/RenderHandler;updateLines()V")
    )
    private void onRenderGameOverlayPostAdvanced(RenderHandler instance, Operation<Void> original) {
        if (Configs.MINI_HUD_FPS_OPT.getBooleanValue()) {
            Thread.startVirtualThread(() -> {
                original.call(instance);
                ThreadUtils.runOnClientThread(() -> {
                        lines.clear();
                        lines.addAll(list);
                }).join();
            });
            return;
        }
        original.call(instance);
    }
}
