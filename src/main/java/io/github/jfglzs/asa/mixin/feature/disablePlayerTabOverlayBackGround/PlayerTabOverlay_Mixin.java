package io.github.jfglzs.asa.mixin.feature.disablePlayerTabOverlayBackGround;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlay_Mixin {
    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getBackgroundColor(I)I")
    )
    public int render(Options instance, int fallbackColor, Operation<Integer> original) {
        var ori = original.call(instance, fallbackColor);
        if (Configs.DISABLE_PLAYER_LIST_HUD_BACKGROUND.getBooleanValue()) {
            return ori & 0x00FFFFFF;
        }
        return ori;
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"
                    )
    )
    private void disableRowBackgroundFill(GuiGraphics instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        if (!Configs.DISABLE_PLAYER_LIST_HUD_BACKGROUND.getBooleanValue()) {
            original.call(instance, x1, y1, x2, y2, color);
        }
    }
}
