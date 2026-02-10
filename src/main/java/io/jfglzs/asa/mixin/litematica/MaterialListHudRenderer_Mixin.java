package io.jfglzs.asa.mixin.litematica;

import fi.dy.masa.litematica.materials.MaterialListHudRenderer;
import io.jfglzs.asa.config.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MaterialListHudRenderer.class)
public class MaterialListHudRenderer_Mixin
{
    @Inject(
            method = "toggleShouldRender",
            at = @At("TAIL"),
            remap = false
    )
    private void toggleShouldRenderInject(CallbackInfo ci) {
        Configs.shouldDisableTitle = !Configs.shouldDisableTitle;
    }
}
