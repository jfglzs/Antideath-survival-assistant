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
    private void redirectUpdateLines(RenderHandler instance) {
        enabled = Configs.MINIHUD_POTIMIZE.getBooleanValue();

        if (!isRunning && enabled) {
            isRunning = true;
            ThreadUtils.threadPool.submit(() -> {
                while (enabled) {
                    try {
                        this.updateLines();
                        Thread.sleep(50); // 每tick刷新一次，节约CPU资源
                    } catch (InterruptedException e) {
                        isRunning = false;
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (!enabled) {
            this.updateLines();
        }
    }

    @Inject(
            method = "updateLines",
            at = @At("TAIL")
    )
    private void onUpdateLinesTail(CallbackInfo ci) {
        if (enabled) {
            List<String> l = List.copyOf(this.lines);
            synchronized (this) {
                this.cache = l;
            }
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
    public int renderText(int x, int y, double fontHeight, int color, int bcolor, HudAlignment alignment, boolean shadow, boolean b2, boolean b3, List<String> list, DrawContext drawContext) {
        if (enabled) {
            List<String> l;
            synchronized (this) {
                // 如果异步线程还没跑完第一次，先用原 list 兜底
                l = this.cache.isEmpty() ? list : this.cache;
            }
            // 调用原本的工具类方法，传入我们的异步快照
            return RenderUtils.renderText(x, y, fontHeight, color, bcolor, alignment, shadow, b2, b3, l, drawContext);
        }
        return RenderUtils.renderText(x, y, fontHeight, color, bcolor, alignment, shadow, b2, b3, list, drawContext);
    }
}
