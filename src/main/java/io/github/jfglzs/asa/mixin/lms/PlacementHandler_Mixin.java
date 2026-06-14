package io.github.jfglzs.asa.mixin.lms;

import fi.dy.masa.litematica.util.PlacementHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlacementHandler.class)
public class PlacementHandler_Mixin {
    @Inject(
            method = "applyPlacementProtocolToPlacementState",
            at = @At("HEAD")
    )
    private static void applyPlacementProtocolToPlacementState(BlockState state, PlacementHandler.UseContext context, CallbackInfoReturnable<BlockState> cir) {
//        System.out.println(BuiltInRegistries.BLOCK.getKey(state.getBlock()));
//        context.getClass()
    }
}
