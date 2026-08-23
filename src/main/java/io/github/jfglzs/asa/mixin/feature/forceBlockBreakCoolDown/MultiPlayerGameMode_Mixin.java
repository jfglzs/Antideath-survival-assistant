package io.github.jfglzs.asa.mixin.feature.forceBlockBreakCoolDown;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static fi.dy.masa.tweakeroo.config.Configs.Disable.DISABLE_BLOCK_BREAK_COOLDOWN;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameMode_Mixin {
    @Shadow
    private int destroyDelay;

    @Inject(
            //~ if >= 26.1 'method_41930' -> 'lambda$continueDestroyBlock$0' {
            method = "lambda$continueDestroyBlock$0",
            //~}
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyBlock(Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private void continueDestroyBlock(CallbackInfoReturnable<Packet> cir) {
        if (Configs.FORCE_BLOCK_BREAK_COOL_DOWN.getBooleanValue()) {
            DISABLE_BLOCK_BREAK_COOLDOWN.setBooleanValue(false);
            this.destroyDelay = 5;
        }
    }
}
