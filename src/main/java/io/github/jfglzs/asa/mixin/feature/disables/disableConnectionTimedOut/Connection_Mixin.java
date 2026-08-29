package io.github.jfglzs.asa.mixin.feature.disables.disableConnectionTimedOut;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class Connection_Mixin {
    @Inject(
            method = "exceptionCaught",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void exceptionCaughtInject(CallbackInfo ci) {
        if (Configs.Disables.DISABLE_CONNECT_TIMED_OUT.getBooleanValue()) {
            ci.cancel();
        }
    }
}
