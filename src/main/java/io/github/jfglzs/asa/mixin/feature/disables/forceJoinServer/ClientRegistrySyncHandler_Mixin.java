package io.github.jfglzs.asa.mixin.feature.disables.forceJoinServer;

import io.github.jfglzs.asa.config.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
//? if = 1.21.1 {
/*import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.impl.registry.sync.SyncCompletePayload;
*///?}
//~ if > 1.21.1 'FabricRegistryClientInit' -> 'ClientRegistrySyncHandler'{
import net.fabricmc.fabric.impl.client.registry.sync.ClientRegistrySyncHandler;

@Mixin(ClientRegistrySyncHandler.class)
//~}
public class ClientRegistrySyncHandler_Mixin {
    //? if > 1.21.1 {
    @Inject(
            method = "apply",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void apply(CallbackInfo ci) {
        if (Configs.Disables.FORCE_JOIN_SERVER_IGNORE_UNKNOWN_PACKET.getBooleanValue()) {
            ci.cancel();
        }
    }
    //?} else {
    /*@Inject(
            method = "lambda$registerSyncPacketReceiver$1",
            at = @At("HEAD"),
            cancellable = true
    )
    private void lambda$registerSyncPacketReceiver$1(ClientConfigurationNetworking.Context context, Boolean complete, Throwable throwable, CallbackInfo ci) {
        if (Configs.Disables.FORCE_JOIN_SERVER_IGNORE_UNKNOWN_PACKET.getBooleanValue()) {
            context.responseSender().sendPacket(SyncCompletePayload.INSTANCE);
            ci.cancel();
        }
    }
    *///?}
}
