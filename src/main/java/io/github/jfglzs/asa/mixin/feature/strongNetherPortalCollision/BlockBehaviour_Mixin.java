package io.github.jfglzs.asa.mixin.feature.strongNetherPortalCollision;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviour_Mixin {
    @Inject(
            method = "getCollisionShape",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (Configs.STRONG_PORTAL_COLLISION.getBooleanValue()) {
            if (state.getBlock() instanceof NetherPortalBlock || state.getBlock() instanceof EndPortalBlock) {
                cir.setReturnValue(Shapes.block());
            }
        }
    }
}
