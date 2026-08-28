package io.github.jfglzs.asa.mixin.feature.optimizations.optDirection;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Direction.class)
public class Direction_Mixin {
    @Inject(
            method = "byName",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void byName(String name, CallbackInfoReturnable<Direction> cir) {
        if (! Configs.OPT_DIRECTION.getBooleanValue())
            return;
        switch (name) {
            case "up" -> cir.setReturnValue(Direction.UP);
            case "down" -> cir.setReturnValue(Direction.DOWN);
            case "north" -> cir.setReturnValue(Direction.NORTH);
            case "south" -> cir.setReturnValue(Direction.SOUTH);
            case "west" -> cir.setReturnValue(Direction.WEST);
            case "east" -> cir.setReturnValue(Direction.EAST);
        }
    }
}
