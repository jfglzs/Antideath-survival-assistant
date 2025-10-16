package io.github.jfglzs.mixin.feature;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.AsaMod.LOGGER_ASA;
import static io.github.jfglzs.feature.totemrestock.TomtemRestock.trigger;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandler_Mixin
{
    @Inject(method = "onEntityStatus", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;getActiveDeathProtector(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;"))
    private static void onEntityStatusInject(EntityStatusS2CPacket packet, CallbackInfo ci)
    {
        LOGGER_ASA.info("Totem has used by player");
        trigger();
    }
}
