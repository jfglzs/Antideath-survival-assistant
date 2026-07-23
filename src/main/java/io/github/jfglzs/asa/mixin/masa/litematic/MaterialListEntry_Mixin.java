package io.github.jfglzs.asa.mixin.masa.litematic;

import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.malilib.util.data.ItemType;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
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
        if (Configs.LITEMATICA_CACULATE_QWP.getBooleanValue()) {
            int value = cir.getReturnValue();
            value = value + ItemStorageDataManager.getRemainCount(this.item.getStack());
            cir.setReturnValue(value);
        }
    }
}
