package io.github.jfglzs.asa.mixin.feature.customBlockCollision;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockCollisions.class)
public class BlockCollisions_Mixin {
    @WrapOperation(
            method = "computeNext",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/shapes/CollisionContext;getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"
            )
    )
    private VoxelShape getCollisionShape(
            CollisionContext context, BlockState state, CollisionGetter getter, BlockPos pos, Operation<VoxelShape> original
    ) {
        if (Configs.ENABLE_STRONG_BLOCK_COLLISION.getBooleanValue() && context instanceof EntityCollisionContext ectx) {
            if (ectx.getEntity() != MCUtils.getPlayer()) {
                return original.call(context, state, getter, pos);
            }
            String blockID = MCUtils.getBlockID(state.getBlock());
            if (Configs.ENABLE_STRONG_BLOCK_COLLISION_WHITELIST.getBooleanValue()) {
                if (Configs.STRONG_BLOCK_COLLISION_WHITELIST.getStrings().stream().anyMatch(blockID::equals)) {
                    return Shapes.block();
                }
            }
            else if (Configs.ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST.getBooleanValue()) {
                if (Configs.STRONG_BLOCK_COLLISION_BLACKLIST.getStrings().stream().noneMatch(blockID::equals)) {
                    return Shapes.block();
                }
            }
        }
        return original.call(context, state, getter, pos);
    }
}
