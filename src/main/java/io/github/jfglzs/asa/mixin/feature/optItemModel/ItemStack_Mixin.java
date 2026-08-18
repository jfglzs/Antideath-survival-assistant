package io.github.jfglzs.asa.mixin.feature.optItemModel;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public class ItemStack_Mixin {
    @Unique
    private boolean asa$hasCached = false;
    @Unique
    private boolean asa$cached = false;

    @WrapMethod(
            method = "hasFoil"
    )
    private boolean hasFoil(Operation<Boolean> original) {
        if (Configs.OPT_ITEM_MODEL.getBooleanValue() && asa$hasCached) {
            return asa$cached;
        }
        asa$hasCached = true;
        asa$cached = original.call();
        return asa$cached;
    }
}
