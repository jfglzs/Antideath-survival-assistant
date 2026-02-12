package io.jfglzs.asa.mixin.minihud;

import fi.dy.masa.malilib.config.HudAlignment;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.minihud.event.RenderHandler;
import io.jfglzs.asa.config.Configs;
import io.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderHandler.class)
public abstract class RenderHandler_Mixin {
    @Shadow
    @Final
    private List<String> lines;
    @Unique
    private List<String> cache = new ArrayList<>();
    @Unique
    private volatile boolean isRunning = false;
    @Unique
    private volatile boolean enabled = false;

    @Shadow
    protected abstract void updateLines();

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
                        isRunning = false;
                        Thread.currentThread().interrupt();
                    }
                }
                isRunning = false;
            });
        } else if (!enabled) {
            this.updateLines();
        }
    }

    @Inject(
            method = "updateLines",
            at = @At("TAIL")
    )
    private void minihudOpt_inject(CallbackInfo ci) {
        if (enabled) return;
        List<String> l = List.copyOf(this.lines);
        synchronized (this) {
            this.cache = l;
        }
    }

    //#if MC >= 12105
    @Redirect(
            method = "onRenderGameOverlayPostAdvanced",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/render/RenderUtils;renderText(IIDIILfi/dy/masa/malilib/config/HudAlignment;ZZZLjava/util/List;Lnet/minecraft/client/gui/DrawContext;)I"
            )
    )
    //#else
    //$$    @Redirect(
    //$$            method = "onRenderGameOverlayPost",
    //$$            at = @At(value = "INVOKE", target = "Lfi/dy/masa/malilib/render/RenderUtils;renderText(IIDIILfi/dy/masa/malilib/config/HudAlignment;ZZZLjava/util/List;Lnet/minecraft/client/gui/DrawContext;)I")
    //$$    )
    //#endif
    public int minihudOpt_redirect(int x, int y, double f, int c, int bx, HudAlignment a, boolean b, boolean b2, boolean b3, List<String> l2, DrawContext cn) {
        List<String> l;
        synchronized (this) {
            // 如果异步线程还没跑完第一次，先用原 list 兜底
            l = this.cache.isEmpty() || !enabled ? l2 : this.cache;
        }
        return RenderUtils.renderText(x, y, f, c, bx, a, b, b2, b3, l, cn);
    }
}
