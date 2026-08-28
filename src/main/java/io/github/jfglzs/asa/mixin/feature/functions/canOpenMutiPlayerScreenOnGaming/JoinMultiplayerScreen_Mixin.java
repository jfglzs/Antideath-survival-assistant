package io.github.jfglzs.asa.mixin.feature.functions.canOpenMutiPlayerScreenOnGaming;

import io.github.jfglzs.asa.accessor.IJoinMultiPlayerScreenAccessor;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class JoinMultiplayerScreen_Mixin implements IJoinMultiPlayerScreenAccessor {
    @Mutable
    @Shadow
    @Final
    private Screen lastScreen;
    @Unique private boolean asa$canDisconnect = false;

    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    private void init(CallbackInfo ci) {
        //兼容viafabricplus
        if (this.lastScreen != null && Configs.CAN_OPEN_MUTI_PLAYER_SCREEN_ON_GAMING.getBooleanValue()) {
            this.lastScreen = MCUtils.getLocalPlayer() == null ? this.lastScreen : null;
        }
    }

    @Inject(
            method = "join",
            at = @At("HEAD")
    )
    private void join(CallbackInfo ci) {
        if (Configs.CAN_OPEN_MUTI_PLAYER_SCREEN_ON_GAMING.getBooleanValue() && this.asa$canDisconnect) {
            MCUtils.disconnect();
            this.asa$canDisconnect = false;
        }
    }

    @Override
    public void asa$setCanDisconnect() {
        this.asa$canDisconnect = true;
    }
}
