package io.github.jfglzs.asa.mixin.litematic;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.util.WorldUtils;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldUtils.class)
public class WorldUtils_Mixin {
    @Inject(
            method = "doSchematicWorldPickBlock",
            at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/schematic/pickblock/SchematicPickBlockEventHandler;onSchematicPickBlockPrePick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/item/ItemStack;)Z")
    )
    private static void doSchematicWorldPickBlock_Inject(boolean closest, MinecraftClient mc, CallbackInfoReturnable<Boolean> cir, @Local ItemStack stack, @Local BlockPos pos) {
        if (PlayerUtils.checkRemainCount(stack.getItem()) == 0 && !mc.player.isCreative() && mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            MaterialToDoRenderer.INSTANCE.addItem(stack);
        }
    }
}
