package io.github.jfglzs.mixin.feature;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.config.Configs.DISABLE_CONNECT_TIMED_OUT;

@Mixin(ClientConnection.class)
public class ClientConnection_Mixin
{
    @Inject(method = "exceptionCaught" , at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;disconnect(Lnet/minecraft/text/Text;)V" , ordinal = 0) , cancellable = true)
    public void exceptionCaught(ChannelHandlerContext context, Throwable ex, CallbackInfo ci)
    {
        if (DISABLE_CONNECT_TIMED_OUT.getBooleanValue())
        {
            ci.cancel();
        }
    }
}
