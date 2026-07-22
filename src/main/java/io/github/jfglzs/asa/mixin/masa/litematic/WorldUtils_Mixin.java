package io.github.jfglzs.asa.mixin.masa.litematic;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.util.WorldUtils;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
                    *///?} else {
                    target = "Lfi/dy/masa/litematica/materials/MaterialCache;getRequiredBuildItemForState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/item/ItemStack;"
                    //?}
                    )
    )
    //? if > 1.21.10 {
    private static ItemStack getRequiredBuildItemForState(MaterialCache instance, BlockState state, Level world, BlockPos pos, Operation<ItemStack> original) {
    //?} else {
    /*private static ItemStack getRequiredBuildItemForState(MaterialCache instance, BlockState state, Operation<ItemStack> original) {
    *///?}
        if (Configs.CUSTOM_LITEMATICA_BLOCK_REPLACE.getBooleanValue()) {
            Map<String, String> mappings = asa$getBlockMappings();
            String oriBlockID = MCUtils.getBlockID(state.getBlock());
            String replacedBlockId = mappings.get(oriBlockID);
            if (replacedBlockId != null) {
                AsaMod.debugMessage("Replaced block：" + oriBlockID + " -> " + replacedBlockId);
                return new ItemStack(MCUtils.getBlock(replacedBlockId));
            }
        }
        //? if < 1.21.11 {
        /*return original.call(instance, state);
        *///?} else {
        return original.call(instance, state, world, pos);
        //?}
    }

    @Inject(
            method = "doSchematicWorldPickBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/schematic/pickblock/SchematicPickBlockEventHandler;onSchematicPickBlockPrePick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    private static void doSchematicWorldPickBlock_Inject(
            boolean closest,
            Minecraft mc,
            CallbackInfoReturnable<Boolean> cir,
            @Local ItemStack stack,
            @Local BlockPos pos
    ) {
        if (
                PlayerUtils.checkRemainCount(stack.getItem()) == 0 &&
                PlayerUtils.isSurvivalMode(mc.player) &&
                mc.level.getBlockState(pos).getBlock() == Blocks.AIR
        ) {
            if (Configs.MID_CLICK_TAKE_ITEM.getBooleanValue()) {
                AsaMod.debugMessage("Submitted %s %d to ItemStorageDataManager");
                ItemStorageDataManager.submit(stack.getItem(), mc.player.isShiftKeyDown() ? stack.getMaxStackSize() * 27 : stack.getMaxStackSize());
            }
            else {
                AsaMod.debugMessage("addItem %s to MaterialToDoRenderer");
                MaterialToDoRenderer.INSTANCE.addItem(stack);
            }
        }
    }


    @Unique
    private static Map<String, String> asa$getBlockMappings() {
        Map<String, String> mappings = new HashMap<>();
        List<String> blockIdMap = Configs.CUSTOM_LITEMATICA_BLOCK_REPLACE_LIST.getStrings();
        for (String blocks : blockIdMap) {
            String[] strings = blocks.split("\\|", 2);
            mappings.put(strings[0], strings[1]);
        }
        return mappings;
    }
}
