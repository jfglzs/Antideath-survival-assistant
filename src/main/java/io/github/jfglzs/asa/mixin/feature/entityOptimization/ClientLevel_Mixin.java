package io.github.jfglzs.asa.mixin.feature.entityOptimization;

import io.github.jfglzs.asa.accessor.EntityAccessor;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevel_Mixin {
    @Inject(
            method = "tickNonPassenger",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tickNonPassenger(Entity entity, CallbackInfo ci) {
        if (Configs.CLIENT_ENTITY_TICK_OPTIMIZATION.getBooleanValue()) {
            boolean canTick = ((EntityAccessor) entity).asa$shouldTick();
            boolean isPlayer = entity instanceof Player;
            if (!canTick && !isPlayer)
                ci.cancel();
        }
    }
}
