package io.github.jfglzs.asa.mixin.feature.optimizations.optSignText;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.chunk.LevelChunk$BoundTickingBlockEntity")
public class BoundTickingBlockEntity_Mixin<T extends BlockEntity> {
    @Shadow
    @Final
    private T blockEntity;

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void tick(CallbackInfo ci) {
        if (this.blockEntity instanceof SignBlockEntity && blockEntity.getLevel() instanceof ClientLevel) {
            ci.cancel();
        }
    }
}
