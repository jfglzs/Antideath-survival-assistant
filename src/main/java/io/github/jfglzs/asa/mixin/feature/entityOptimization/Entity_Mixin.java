package io.github.jfglzs.asa.mixin.feature.entityOptimization;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.EntityUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class Entity_Mixin {
    @Shadow
    private Level level;

    //~ if > 1.21.1 'checkInsideBlocks' -> 'applyEffectsFromBlocks()V' {
    @Inject(
            method = "applyEffectsFromBlocks()V",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void applyEffectsFromBlocks(CallbackInfo ci) {
        if (asa$canDisable(level)) {
            boolean isPlayer = ((Object) this) instanceof Player;
            if (!isPlayer) ci.cancel();
        }
    }
    //~}

    @Inject(
            method = "collide",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void collide(Vec3 movement, CallbackInfoReturnable<Vec3> cir) {
        Object self = this;
        if (asa$canDisable(level) && !EntityUtils.isPlayer(self) && !(self instanceof ItemEntity)) {
            cir.setReturnValue(Vec3.ZERO);
        }
    }

    @Inject(
            method = "isVehicle",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void isVehicle(CallbackInfoReturnable<Boolean> cir) {
        if (asa$canDisable(level)) {
            boolean isVehicle = ((Object) this) instanceof VehicleEntity;
            cir.setReturnValue(isVehicle);
        }
    }

    @Unique
    private static boolean asa$canDisable(Level level) {
        return level.isClientSide() && Configs.CLIENT_ENTITY_TICK_OPTIMIZATION.getBooleanValue();
    }
}
