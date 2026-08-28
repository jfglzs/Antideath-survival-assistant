package io.github.jfglzs.asa.mixin.feature.disables.disableLoadingScreen;

import io.github.jfglzs.asa.config.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//~ if >= 1.21.8 'ReceivingLevelScreen' -> 'LevelLoadingScreen' {
@Mixin(net.minecraft.client.gui.screens.LevelLoadingScreen.class)
//~}
public abstract class ReceivingLevelScreen_Mixin {
    //~ if >= 26.1.2 'render' -> 'extractBackground' {
    @Inject(
            method = "extractBackground",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
    )
    public void extractBackground(CallbackInfo ci) {
        if (Configs.DISABLE_LOADING_TERRAIN_SCREEN.getBooleanValue()) {
            ci.cancel();
        }
    }
    //~}

    //? if >= 26.1.2 {
    @Inject(
            method = "extractRenderState",
            at = @At("HEAD"),
            cancellable = true
    )
    public void extractRenderState(CallbackInfo ci) {
        if (Configs.DISABLE_LOADING_TERRAIN_SCREEN.getBooleanValue()) {
            ci.cancel();
        }
    }
    //?}
}
