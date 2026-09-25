package io.github.jfglzs.asa.mixin.feature.optimizations.optEntityLight;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.BlockAndLightGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightCoordsUtil.class)
public class LightCoordsUtil_Mixin {
    @Inject(
            //~ if >= 26.2 'getLightColor' -> 'getLightCoords' {
            method = "getLightCoords(Lnet/minecraft/world/level/BlockAndLightGetter;Lnet/minecraft/core/BlockPos;)I",
            //~}
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getLightCoords(BlockAndLightGetter level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (Configs.Optimizations.OPT_ENTITY_LIGHT.getBooleanValue())
            cir.setReturnValue(15728880);
    }
}
