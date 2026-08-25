package io.github.jfglzs.asa.mixin.masa.litematic;

import fi.dy.masa.litematica.materials.MaterialListEntry;
//~ if >= 26.1 'util.ItemType' -> 'util.data.ItemType' {
import fi.dy.masa.malilib.util.data.ItemType;
//~}
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaterialListEntry.class)
public class MaterialListEntry_Mixin {
    @Shadow
    @Final
    private ItemType item;

    @Inject(
            method = "getCountAvailable",
            at = @At("RETURN"),
            cancellable = true
    )
    private void getCountAvailable(CallbackInfoReturnable<Integer> cir) {
        int value = cir.getReturnValue();
        ItemStack stack = this.item.getStack();
        if (Configs.LITEMATICA_CALCULATE_QWP.getBooleanValue()) {
            value = value + ItemStorageDataManager.getCount(stack.getItem(), false);
        }
        if (Configs.LITEMATICA_CALCULATE_FAKE.getBooleanValue()) {
            value = value + ItemStorageDataManager.getCount(stack.getItem(), true);
        }
        cir.setReturnValue(value);
    }
}
