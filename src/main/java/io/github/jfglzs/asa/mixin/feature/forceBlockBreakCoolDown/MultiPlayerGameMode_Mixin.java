package io.github.jfglzs.asa.mixin.feature.forceBlockBreakCoolDown;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static fi.dy.masa.tweakeroo.config.Configs.Disable.DISABLE_BLOCK_BREAK_COOLDOWN;
//? < 26.1
//import net.minecraft.world.level.block.state.BlockState;

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
    //~ if >= 26.1 'BlockState blockState, BlockPos blockPos, Direction direction, int i, CallbackInfoReturnable<Packet> cir' -> 'BlockPos pos, Direction direction, int sequence, CallbackInfoReturnable<Packet> cir' {
    private void continueDestroyBlock(BlockPos pos, Direction direction, int sequence, CallbackInfoReturnable<Packet> cir) {
    //~}
        if (Configs.FORCE_BLOCK_BREAK_COOL_DOWN.getBooleanValue()) {
            DISABLE_BLOCK_BREAK_COOLDOWN.setBooleanValue(false);
            this.destroyDelay = 5;
        }
    }
}
