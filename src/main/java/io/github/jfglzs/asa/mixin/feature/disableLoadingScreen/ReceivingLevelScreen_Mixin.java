package io.github.jfglzs.asa.mixin.feature.disableLoadingScreen;
//~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.asa.config.Configs.DISABLE_LOADING_TERRAIN_SCREEN;
@Mixin(LevelLoadingScreen.class)
public abstract class ReceivingLevelScreen_Mixin {
    //~ if >= 26.1 'render' -> 'extractRenderState' {
    @Inject(
            method = "extractRenderState",
            at = @At("HEAD"),
            cancellable = true
    )
    public void extractRenderState_Inject(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        if (DISABLE_LOADING_TERRAIN_SCREEN.getBooleanValue()) {
            ci.cancel();
        }
    }
    //~}
}
//~}
