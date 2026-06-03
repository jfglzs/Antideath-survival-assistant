package io.github.jfglzs.asa.mixin.feature.disableConnectionTimedOut;

import io.github.jfglzs.asa.config.Configs;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class Connection_Mixin {
    @Inject(
            method = "exceptionCaught",
            at = @At(
                     value = "HEAD"
            ),
            cancellable = true
    )
    public void exceptionCaughtInject(ChannelHandlerContext context, Throwable ex, CallbackInfo ci) {
        if (Configs.DISABLE_CONNECT_TIMED_OUT.getBooleanValue()) {
            ci.cancel();
        }
    }
}
