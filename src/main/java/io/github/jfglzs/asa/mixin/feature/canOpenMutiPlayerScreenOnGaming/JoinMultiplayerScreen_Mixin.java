package io.github.jfglzs.asa.mixin.feature.canOpenMutiPlayerScreenOnGaming;

import io.github.jfglzs.asa.accessor.IJoinMultiPlayerScreen;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class JoinMultiplayerScreen_Mixin implements IJoinMultiPlayerScreen {
    @Unique
    private boolean asa$canDisconnect = false;

    @Inject(
            method = "join",
            at = @At("HEAD")
    )
    private void join(CallbackInfo ci) {
        if (Configs.CAN_OPEN_MUTI_PLAYER_SCREEN_ON_GAMING.getBooleanValue() && this.asa$canDisconnect) {
            //? if < 1.21.8 {
            /*MCUtils.getLevel().disconnect();
            *///?} else if < 1.21.10 {
            /*MCUtils.getLevel().disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
            *///?} else {
            MCUtils.getMinecraft().disconnectFromWorld(ClientLevel.DEFAULT_QUIT_MESSAGE);
            //?}
            this.asa$canDisconnect = false;
        }
    }

    @Override
    public void asa$setCanDisconnect() {
        this.asa$canDisconnect = true;
    }
}
