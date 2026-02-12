package io.jfglzs.asa.mixin.minihud;

import fi.dy.masa.minihud.event.RenderHandler;
import io.jfglzs.asa.config.Configs;
import io.jfglzs.asa.utils.ThreadUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(RenderHandler.class)
public abstract class RenderHandler_Mixin {
    @Shadow @Final @Mutable
    private List<String> lines;
    @Unique
    private boolean isRunning = false;
    @Unique
    private boolean enabled = false;
    @Shadow
    protected abstract void updateLines();

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    public void init_Inject(CallbackInfo ci) {
        this.lines = new CopyOnWriteArrayList<>();
    }

    //#if MC >= 12105
    @Redirect(
            method = "onRenderGameOverlayPostAdvanced",
            at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/event/RenderHandler;updateLines()V")
    )
    //#else
    //$$    @Redirect(
    //$$            method = "onRenderGameOverlayPost",
    //$$            at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/event/RenderHandler;updateLines()V")
    //$$    )
    //#endif
    private void minihudOpt_redirect(RenderHandler instance) {
        enabled = Configs.MINIHUD_POTIMIZE.getBooleanValue();

        if (!isRunning && enabled) {
            isRunning = true;
            ThreadUtils.threadPool.submit(() -> {
                while (enabled) {
                    try {
                        this.updateLines();
                        Thread.sleep(50); // 每tick刷新一次，节约CPU资源
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                isRunning = false;
            });
        } else if (!enabled) {
            this.updateLines();
        }
    }
}
