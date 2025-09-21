package io.github.jfglzs.mixin.feature;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.config.Configs.DISABLE_LOADING_TERRAIN_SCREEN;

@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreen_Mixin
{
    @Inject(method = "render" , at = @At("HEAD") , cancellable = true)
    public void renderInject(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci)
    {
        if (DISABLE_LOADING_TERRAIN_SCREEN.getBooleanValue())
        {
            ci.cancel();
        }
    }

    @Inject(method = "renderBackground" , at = @At("HEAD") , cancellable = true)
    public void renderBackgroundInject(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci)
    {
        if (DISABLE_LOADING_TERRAIN_SCREEN.getBooleanValue())
        {
            ci.cancel();
        }
    }


}
