package io.github.jfglzs.asa.mixin.feature.disablePlayerTabOverlayBackGround;
//~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlay_Mixin {
    //~ if >= 26.1 'render' -> 'extractRenderState'
    @WrapOperation(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"
                    )
    )

    private void disableRowBackgroundFill(GuiGraphicsExtractor instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        if (!Configs.DISABLE_PLAYER_LIST_HUD_BACKGROUND.getBooleanValue()) {
            original.call(instance, x1, y1, x2, y2, color);
        }
    }
}
//~}
