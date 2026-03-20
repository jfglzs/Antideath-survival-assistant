package io.github.jfglzs.asa.mixin.feature;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubtitlesHud.class)
public class SubtitlesHud_Mixin {
    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderInject(DrawContext context, CallbackInfo ci) {
        if (Configs.DISABLE_SUBTITLE.getBooleanValue() && Configs.shouldDisableTitle) {
            ci.cancel();
        }
    }
}
