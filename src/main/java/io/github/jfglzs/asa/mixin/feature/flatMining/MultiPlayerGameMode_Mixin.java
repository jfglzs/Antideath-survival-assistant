package io.github.jfglzs.asa.mixin.feature.flatMining;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameMode_Mixin {
    @Inject(
            method = "startDestroyBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void startDestroyBlock_Inject(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (this.asa$shouldFlatDigger(pos)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "continueDestroyBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void continueDestroyBlock_Inject(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (this.asa$shouldFlatDigger(pos)) {
            cir.setReturnValue(false);
        }
    }


    @Unique
    private boolean asa$shouldFlatDigger(BlockPos pos) {
        var level = Minecraft.getInstance().level;
        var player = Minecraft.getInstance().player;

        if (Configs.FLAT_MINING.getBooleanValue() && level != null && player != null) {
            return !player.isShiftKeyDown() && pos.getY() < player.getBlockY();
        }

        return false;
    }
}
