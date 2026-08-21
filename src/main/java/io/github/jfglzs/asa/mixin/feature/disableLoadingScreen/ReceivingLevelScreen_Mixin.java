package io.github.jfglzs.asa.mixin.feature.disableLoadingScreen;

//~ if >= 1.21.8 'ReceivingLevelScreen' -> 'LevelLoadingScreen' {
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
//~}
public abstract class ReceivingLevelScreen_Mixin {
    //~ if >= 1.21.8 'render' -> 'extractBackground' {
    @Inject(
            method = "extractBackground",
            at = @At("HEAD"),
            cancellable = true
    )
    public void extractBackground(CallbackInfo ci) {
        if (Configs.DISABLE_LOADING_TERRAIN_SCREEN.getBooleanValue()) {
            ci.cancel();
        }
    }
    //~}

    //? if >= 1.21.8 {
    @Inject(
            method = "extractRenderState",
            at = @At("HEAD")
    )
    public void extractRenderState(CallbackInfo ci) {

    }
    //?}
}
