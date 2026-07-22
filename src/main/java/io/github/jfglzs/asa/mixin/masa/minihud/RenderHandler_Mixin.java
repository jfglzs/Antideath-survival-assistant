package io.github.jfglzs.asa.mixin.masa.minihud;


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

@Mixin(value = RenderHandler.class, priority = 1900)
public class RenderHandler_Mixin {
    @Shadow
    @Final
    private List<String> lines;
    @Unique
    private final List<String> asa$list = new ArrayList<>();


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
                ThreadUtils.runOnClientThread(() -> {
                        lines.clear();
                        lines.addAll(asa$list);
                });
            });
            return;
        }
        original.call(instance);
    }
}
