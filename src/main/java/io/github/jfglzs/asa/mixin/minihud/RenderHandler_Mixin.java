package io.github.jfglzs.asa.mixin.minihud;

import fi.dy.masa.minihud.event.RenderHandler;
import io.github.jfglzs.asa.config.Configs;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(RenderHandler.class)
public abstract class RenderHandler_Mixin {
    @Shadow
    @Mutable
    @Final
    private List<String> lines;

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
            //#if MC >= 12105
            method = "onRenderGameOverlayPostAdvanced",
            //#else
            //$$ method = "onRenderGameOverlayPost",
            //#endif
            at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/event/RenderHandler;updateLines()V")
    )
    private void updateLinesRedirect(RenderHandler instance) {
        enabled = Configs.MINIHUD_POTIMIZE.getBooleanValue();
        if (enabled) {
            Thread.startVirtualThread(this::updateLines);
        } else {
            this.updateLines();
        }
    }
}

