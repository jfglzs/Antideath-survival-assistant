package io.github.jfglzs.asa.mixin.litematic;

import fi.dy.masa.litematica.materials.MaterialListHudRenderer;
import io.github.jfglzs.asa.config.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MaterialListHudRenderer.class)
public class MaterialListHudRenderer_Mixin {
    @Inject(
            method = "toggleShouldRender",
            at = @At("HEAD")
    )
    public void toggleShouldRender_Inject(CallbackInfo ci) {
        Configs.shouldDisableTitle = !Configs.shouldDisableTitle;
    }
}
