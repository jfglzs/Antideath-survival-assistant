package io.github.jfglzs.asa.mixin.feature.disableScoreBoardBackGround;

//~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//~ if >= 26.2 'Gui' -> 'Hud' {
@Mixin(net.minecraft.client.gui.Gui.class)
//~}
public class Gui_Mixin {
    @WrapOperation(
            //? if <= 1.21.1 {
            /*method = "method_55440",
            *///?} else {
            method = "displayScoreboardSidebar",
            //?}
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V")
    )
    public void fill(GuiGraphicsExtractor instance, int x0, int y0, int x1, int y1, int col, Operation<Void> original) {
        if (! Configs.DISABLE_SCORE_BOARD_BACK_GROUND.getBooleanValue())
            original.call(instance, x0, y0, x1, y1, col);
    }
}
//~}