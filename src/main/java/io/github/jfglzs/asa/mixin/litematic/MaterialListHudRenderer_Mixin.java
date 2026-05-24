package io.github.jfglzs.asa.mixin.litematic;

import fi.dy.masa.litematica.materials.MaterialListHudRenderer;
import fi.dy.masa.malilib.config.HudAlignment;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
