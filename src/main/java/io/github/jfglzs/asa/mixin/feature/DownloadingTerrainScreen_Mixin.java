package io.github.jfglzs.asa.mixin.feature;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.asa.config.Configs.DISABLE_LOADING_TERRAIN_SCREEN;

@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreen_Mixin {
    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    public void render_Inject(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        if (DISABLE_LOADING_TERRAIN_SCREEN.getBooleanValue()) {
            ci.cancel();
        }
    }

    //? if > 1.21.1 {
    @Inject(
            method = "renderBackground",
            at = @At("HEAD"),
            cancellable = true
    )
    public void renderBackground_Inject(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        if (DISABLE_LOADING_TERRAIN_SCREEN.getBooleanValue()) {
            ci.cancel();
        }
    }
    //?}
}
