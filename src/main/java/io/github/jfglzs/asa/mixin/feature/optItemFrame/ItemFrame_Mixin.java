package io.github.jfglzs.asa.mixin.feature.optItemFrame;

import io.github.jfglzs.asa.ShareConstants;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFrame_Mixin extends Entity {
    public ItemFrame_Mixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(
            method = "getFramedMapId",
            at = @At("RETURN"),
            cancellable = true
    )
    public void getFramedMapId(ItemStack itemStack, CallbackInfoReturnable<MapId> cir) {
        if (Configs.OPT_ITEM_FRAME.getBooleanValue()) {
            int stackHashCode = itemStack.hashCode();
            MapId mapId = ShareConstants.ITEM_STACK_MAP_CACHE.get(stackHashCode);
            if (mapId != null) {
                cir.setReturnValue(mapId);
            }
            else {
                MapId returnValue = cir.getReturnValue();
                ShareConstants.ITEM_STACK_MAP_CACHE.put(stackHashCode, returnValue);
            }
        }
    }

    @Override
    public boolean isDiscrete() {
        return !Configs.OPT_ITEM_FRAME.getBooleanValue() && super.isDiscrete();
    }

    @Override
    public boolean fireImmune() {
        return !Configs.OPT_ITEM_FRAME.getBooleanValue() && super.fireImmune();
    }
}
