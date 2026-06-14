package io.github.jfglzs.asa.mixin.feature.canAlwaysDisconnect;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.multiplayer.ServerReconfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerReconfigScreen.class)
public class ServerReconfigScreen_Mixin {
    @Shadow
    private Button disconnectButton;

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void tick(CallbackInfo ci) {
        if (Configs.CAN_ALWAYS_DISCONNECT.getBooleanValue()) disconnectButton.active = true;
    }
}
