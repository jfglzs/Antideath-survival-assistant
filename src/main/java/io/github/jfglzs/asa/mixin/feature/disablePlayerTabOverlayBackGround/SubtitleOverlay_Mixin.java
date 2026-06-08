package io.github.jfglzs.asa.mixin.feature.disablePlayerTabOverlayBackGround;
//~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.SubtitleOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SubtitleOverlay.class)
public class SubtitleOverlay_Mixin {
    //~ if >= 26.1 'render' -> 'extractRenderState' {
    @WrapOperation(
            method = "extractRenderState",
    //~}
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V")
    )
    private void extractRenderStateWrap(GuiGraphicsExtractor instance, int x0, int y0, int x1, int y1, int col, Operation<Void> original) {
        if (!Configs.DISABLE_SUBTITLE_OVERLAY_BACKGROUND.getBooleanValue()) original.call(instance, x0, y0, x1, y1, col);
    }
}
//~}
