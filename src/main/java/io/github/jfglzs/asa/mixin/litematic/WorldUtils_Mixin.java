package io.github.jfglzs.asa.mixin.litematic;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.util.WorldUtils;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldUtils.class)
public class WorldUtils_Mixin {
    @Inject(
            method = "doSchematicWorldPickBlock",
            at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/schematic/pickblock/SchematicPickBlockEventHandler;onSchematicPickBlockPrePick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private static void doSchematicWorldPickBlock_Inject(boolean closest, Minecraft mc, CallbackInfoReturnable<Boolean> cir, @Local ItemStack stack, @Local BlockPos pos) {
        if (PlayerUtils.checkRemainCount(stack.getItem()) == 0 && !mc.player.isCreative() && mc.level.getBlockState(pos).getBlock() == Blocks.AIR) {
            if (Configs.MID_CLICK_TAKE_ITEM.getBooleanValue()) {
                ItemStorageDataManager.submit(stack.getItem(), stack.getMaxStackSize());
            }
            else {
                MaterialToDoRenderer.INSTANCE.addItem(stack);
            }
        }
    }
}
