package io.github.jfglzs.asa.mixin.feature.disablePlayerListHudBackGround;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerListHud.class)
public class PlayerListHud_Mixin {
    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getTextBackgroundColor(I)I")
    )
    public int render(GameOptions instance, int fallbackColor, Operation<Integer> original) {
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
                    target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"
                    )
    )
    private void disableRowBackgroundFill(DrawContext instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        if (!Configs.DISABLE_PLAYER_LIST_HUD_BACKGROUND.getBooleanValue()) {
            original.call(instance, x1, y1, x2, y2, color);
        }
    }
}
