package io.github.jfglzs.asa.mixin.feature.disables.disableConnectionTimedOut;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.TimeoutException;

@Mixin(Connection.class)
public abstract class Connection_Mixin {
    @Inject(
            method = "exceptionCaught",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void exceptionCaughtInject(CallbackInfo ci, @Local Throwable cause) {
        if (cause instanceof TimeoutException && Configs.Disables.DISABLE_CONNECT_TIMED_OUT.getBooleanValue()) {
            ci.cancel();
        }
    }
}
