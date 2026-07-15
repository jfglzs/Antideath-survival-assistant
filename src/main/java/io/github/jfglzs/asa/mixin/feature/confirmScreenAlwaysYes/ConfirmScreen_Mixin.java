package io.github.jfglzs.asa.mixin.feature.confirmScreenAlwaysYes;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screens.ConfirmScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfirmScreen.class)
public class ConfirmScreen_Mixin {
    @Shadow
    @Final
    protected BooleanConsumer callback;

    @Inject(
            method = "init",
            at = @At("HEAD")
    )
    private void init(CallbackInfo ci) {
        if (Configs.CONFIRM_SCREEN_ALWAYS_YES.getBooleanValue()) {
            this.callback.accept(true);
            MCUtils.setScreen(null);
        }
    }
}
