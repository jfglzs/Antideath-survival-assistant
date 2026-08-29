package io.github.jfglzs.asa.mixin.feature.functions.customLitematicaBlockReplace;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.util.WorldUtils;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(WorldUtils.class)
public class WorldUtils_Mixin {
    @WrapOperation(
            method = "doEasyPlaceAction",
            at = @At(
                    value = "INVOKE",
                    //? if < 1.21.11 {
                    /*target = "Lfi/dy/masa/litematica/materials/MaterialCache;getRequiredBuildItemForState(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;"
                    */
                    //?} else {
                    target = "Lfi/dy/masa/litematica/materials/MaterialCache;getRequiredBuildItemForState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/item/ItemStack;"
                    //?}
                    )
    )
    //? if > 1.21.10 {
    private static ItemStack getRequiredBuildItemForState(MaterialCache instance, BlockState state, Level world,
                                                          BlockPos pos, Operation<ItemStack> original) {
        //?} else {
        /*private static ItemStack getRequiredBuildItemForState(MaterialCache instance, BlockState state, Operation<ItemStack> original) {
         *///?}
        if (Configs.Functions.CUSTOM_LITEMATICA_BLOCK_REPLACE.getBooleanValue()) {
            Map<String, String> mappings = asa$getBlockMappings();
            String oriBlockID = MCUtils.getBlockID(state.getBlock());
            String replacedBlockId = mappings.get(oriBlockID);
            if (replacedBlockId != null) {
                AsaMod.debugMessage(() -> "Replaced block：" + oriBlockID + " -> " + replacedBlockId);
                return new ItemStack(MCUtils.getBlock(replacedBlockId));
            }
        }
        //? if < 1.21.11 {
        /*return original.call(instance, state);
         *///?} else {
        return original.call(instance, state, world, pos);
        //?}
    }

    @Unique
    private static Map<String, String> asa$getBlockMappings() {
        Map<String, String> mappings = new HashMap<>();
        List<String> blockIdMap = Configs.Lists.CUSTOM_LITEMATICA_BLOCK_REPLACE_LIST.getStrings();
        for (String blocks : blockIdMap) {
            String[] strings = blocks.split("\\|", 2);
            mappings.put(strings[0], strings[1]);
        }
        return mappings;
    }
}
