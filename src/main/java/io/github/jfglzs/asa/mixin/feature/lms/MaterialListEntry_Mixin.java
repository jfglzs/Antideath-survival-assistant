package io.github.jfglzs.asa.mixin.feature.lms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MaterialListEntry.class)
public class MaterialListEntry_Mixin {
    @Shadow
    @Final
    //~ if >= 26.1 'util.ItemType' -> 'util.data.ItemType' {
    private fi.dy.masa.malilib.util.data.ItemType item;
    //~}


    @ModifyReturnValue(
            method = "getCountAvailable",
            at = @At("RETURN")
    )
    private int getCountAvailable(int original) {
        int value = original;
        ItemStack stack = this.item.getStack();

        if (Configs.LMS.LITEMATICA_CALCULATE_QWP.getBooleanValue())
            value = value + ItemStorageDataManager.getCount(stack.getItem(), false);
        if (Configs.LMS.LITEMATICA_CALCULATE_FAKE.getBooleanValue())
            value = value + ItemStorageDataManager.getCount(stack.getItem(), true);

        return value;
    }
}
