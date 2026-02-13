package xyz.antideath.asa.mixin.feature;

import xyz.antideath.asa.config.Configs;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnection_Mixin {
    @Inject(
            method = "exceptionCaught",
            at = @At(
                     value = "INVOKE",
                     target = "Lnet/minecraft/network/ClientConnection;disconnect(Lnet/minecraft/text/Text;)V" ,
                     ordinal = 0
            ),
            cancellable = true
    )
    public void exceptionCaughtInject(ChannelHandlerContext context, Throwable ex, CallbackInfo ci) {
        if (Configs.DISABLE_CONNECT_TIMED_OUT.getBooleanValue()) {
            ci.cancel();
        }
    }
}
