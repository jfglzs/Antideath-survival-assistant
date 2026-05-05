package io.github.jfglzs.asa.mixin.litematic;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.util.WorldUtils;
import io.github.jfglzs.asa.feature.itemdisplay.RemainingItemDisplayer;
import io.github.jfglzs.asa.render.MaterialToDoRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
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
    private static void doSchematicWorldPickBlock_Inject(boolean closest, MinecraftClient mc, CallbackInfoReturnable<Boolean> cir, @Local(name = "stack") ItemStack stack) {
        if (RemainingItemDisplayer.checkRemainCount(stack.getItem()) == 0 && !mc.player.isCreative()) {
            MaterialToDoRenderer.INSTANCE.addItem(stack);
        }
    }
}
