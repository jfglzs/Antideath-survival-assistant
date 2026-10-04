package io.github.jfglzs.asa.mixin.feature.optimizations.optSignText;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayer_Mixin {
    @Inject(
            method = "isTextFilteringEnabled",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isTextFilteringEnabled(CallbackInfoReturnable<Boolean> cir) {
        if (Configs.Optimizations.OPT_SIGN_TEXT.getBooleanValue())
            cir.setReturnValue(false);
    }
}
