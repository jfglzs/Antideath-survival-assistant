package io.github.jfglzs.asa.mixin.feature.disables.disableSubtitle;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.components.SubtitleOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubtitleOverlay.class)
public class SubtitleOverlay_Mixin {
    //~ if >= 26.1 'render' -> 'extractRenderState' {
    @Inject(
            method = "extractRenderState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void extractRenderState(CallbackInfo ci) {
        if (Configs.Disables.DISABLE_SUBTITLE.getBooleanValue() && Configs.shouldDisableTitle) {
            ci.cancel();
        }
    }
    //~}
}

