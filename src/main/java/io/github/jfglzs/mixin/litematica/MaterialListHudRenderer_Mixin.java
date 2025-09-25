package io.github.jfglzs.mixin.litematica;

import fi.dy.masa.litematica.materials.MaterialListHudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.config.Configs.shouldDisableTitle;

@Mixin(MaterialListHudRenderer.class)
public class MaterialListHudRenderer_Mixin
{
    @Inject(method = "toggleShouldRender" , at = @At("TAIL") , remap = false)
    private void toggleShouldRenderInject(CallbackInfo ci)
    {
        shouldDisableTitle = !shouldDisableTitle;
    }
}
