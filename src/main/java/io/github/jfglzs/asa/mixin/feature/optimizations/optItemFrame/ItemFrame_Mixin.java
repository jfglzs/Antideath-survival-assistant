package io.github.jfglzs.asa.mixin.feature.optimizations.optItemFrame;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;

@Mixin(ItemFrame.class)
public abstract class ItemFrame_Mixin extends Entity {
    public ItemFrame_Mixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isDiscrete() {
        return ! Configs.OPT_ITEM_FRAME.getBooleanValue() && super.isDiscrete();
    }

    @Override
    public boolean fireImmune() {
        return ! Configs.OPT_ITEM_FRAME.getBooleanValue() && super.fireImmune();
    }
}
