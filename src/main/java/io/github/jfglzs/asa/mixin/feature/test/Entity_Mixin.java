package io.github.jfglzs.asa.mixin.feature.test;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class Entity_Mixin {
//    @Shadow
//    private Level level;
//
//    @Inject(
//            method = "collide",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    public void tick(Vec3 movement, CallbackInfoReturnable<Vec3> cir) {
//        if (((Object) this) instanceof Player) return;
//        if (this.level.isClientSide() && Configs.DEBUG.getBooleanValue()) {
//            cir.setReturnValue(Vec3.ZERO);
//        }
//    }
//
//    @Inject(
//            method = "isInvisibleTo",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    public void isInvisibleTo(Player player, CallbackInfoReturnable<Boolean> cir) {
//        if (((Object) this) instanceof Player) return;
//        if (this.level.isClientSide() && Configs.DEBUG.getBooleanValue()) {
//            cir.setReturnValue(false);
//        }
//    }
//
//    @Inject(
//            method = "checkInsideBlocks(Ljava/util/List;Lnet/minecraft/world/entity/InsideBlockEffectApplier$StepBasedCollector;)V",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void applyEffectsFromBlocks(CallbackInfo ci) {
//        if (((Object) this) instanceof Player) return;
//        if (this.level.isClientSide() && Configs.DEBUG.getBooleanValue()) {
//            ci.cancel();
//        }
//    }
}
