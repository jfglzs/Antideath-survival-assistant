package io.github.jfglzs.asa.mixin.feature.disableContainerBackGround;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class Screen_Mixin {
    //? if > 1.21.10 {
    //~ if >= 26.1 'render' -> 'extract' {
    @Inject(
            method = "extractTransparentBackground",
            at = @At("HEAD"),
            cancellable = true
    )
    private void extractTransparentBackground(CallbackInfo ci) {
        //~}
        if (Configs.DISABLE_CONTAINER_BACKGROUND.getBooleanValue())
            ci.cancel();
    }
    //?}
}