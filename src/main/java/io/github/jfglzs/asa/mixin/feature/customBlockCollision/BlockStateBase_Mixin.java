package io.github.jfglzs.asa.mixin.feature.customBlockCollision;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateBase_Mixin {
    @Inject(
            method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext ctx, CallbackInfoReturnable<VoxelShape> cir) {
        if (Configs.ENABLE_STRONG_BLOCK_COLLISION.getBooleanValue()) {
            EntityCollisionContext context = asa$getEntityCollisionContext(ctx);
            if (context == null || context.getEntity() != MCUtils.getLocalPlayer()) {
                return;
            }

            BlockState state = level.getBlockState(pos);
            String blockID = MCUtils.getBlockID(state.getBlock());

            if (Configs.ENABLE_STRONG_BLOCK_COLLISION_WHITELIST.getBooleanValue()) {
                if (Configs.STRONG_BLOCK_COLLISION_WHITELIST.getStrings().stream().anyMatch(blockID::equals)) {
                    cir.setReturnValue(Shapes.block());
                }
            }
            else if (Configs.ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST.getBooleanValue()) {
                if (Configs.STRONG_BLOCK_COLLISION_BLACKLIST.getStrings().stream().noneMatch(blockID::equals)) {
                    cir.setReturnValue(Shapes.block());
                }
            }
        }
    }

    @Unique
    private EntityCollisionContext asa$getEntityCollisionContext(CollisionContext context) {
        if (MCUtils.isModLoaded("lithium")) {
            return context.getClass() == EntityCollisionContext.class ? (EntityCollisionContext) context : null;
        }
        else if (context instanceof EntityCollisionContext) {
            return (EntityCollisionContext) context;
        }
        return null;
    }
}
