//package io.github.jfglzs.asa.mixin.feature.customBlockCollision;
//
//import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import io.github.jfglzs.asa.config.Configs;
//import io.github.jfglzs.asa.utils.MCUtils;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.level.CollisionGetter;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.shapes.EntityCollisionContext;
//import net.minecraft.world.phys.shapes.Shapes;
//import net.minecraft.world.phys.shapes.VoxelShape;
//import org.jetbrains.annotations.Nullable;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//
//@Mixin(EntityCollisionContext.class)
//public class EntityCollisionContext_Mixin {
//    @Shadow
//    @Final
//    @Nullable
//    private Entity entity;
//
//    @WrapMethod(
//            method = "getCollisionShape"
//    )
//    private VoxelShape getCollisionShape(BlockState state, CollisionGetter getter, BlockPos pos, Operation<VoxelShape> original) {
//
//        if (Configs.ENABLE_STRONG_BLOCK_COLLISION.getBooleanValue()) {
//            if (this.entity != MCUtils.getLocalPlayer()) {
//                //? if > 1.21.1 {
//                /*return original.call(state, getter, pos);
//                *///?} else {
//                return original.call(state, getter, pos);
//                //?}
//            }
//
//            String blockID = MCUtils.getBlockID(state.getBlock());
//
//            if (Configs.ENABLE_STRONG_BLOCK_COLLISION_WHITELIST.getBooleanValue()) {
//                if (Configs.STRONG_BLOCK_COLLISION_WHITELIST.getStrings().stream().anyMatch(blockID::equals)) {
//                    return Shapes.block();
//                }
//            }
//            else if (Configs.ENABLE_STRONG_BLOCK_COLLISION_BLACKLIST.getBooleanValue()) {
//                if (Configs.STRONG_BLOCK_COLLISION_BLACKLIST.getStrings().stream().noneMatch(blockID::equals)) {
//                    return Shapes.block();
//                }
//            }
//        }
//        return  original.call(state, getter, pos);
//    }
//}
