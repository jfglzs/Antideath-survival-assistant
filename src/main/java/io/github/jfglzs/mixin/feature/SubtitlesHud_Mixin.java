package io.github.jfglzs.mixin.feature;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.config.Configs.DISABLE_SUBTITLE;
import static io.github.jfglzs.config.Configs.shouldDisableTitle;

@Mixin(SubtitlesHud.class)
public class SubtitlesHud_Mixin
{
    @Inject(method = "render" , at = @At("HEAD"),cancellable = true)
    private void renderInject(DrawContext context, CallbackInfo ci)
    {
        if (DISABLE_SUBTITLE.getBooleanValue() && shouldDisableTitle)
        {
            ci.cancel();
        }
    }
}
