package io.github.jfglzs.asa.mixin.feature.functions.confirmScreenAlwaysYes;

//? if >= 1.21.10 {
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListener_Mixin {

    @Inject(
            method = "openSendConfirmationWindow",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onOpenSendConfirmationWindow(String command, String messageKey, Component acceptButton,
                                              Runnable onAccept, CallbackInfo ci) {
        if (Configs.Disables.CONFIRM_SCREEN_ALWAYS_YES.getBooleanValue()) {
            onAccept.run();
            ci.cancel();
        }
    }
}
//?} else {
/*@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.asa.utils.DummyClass.class)
public class ClientPacketListener_Mixin {
}*/
//?}

