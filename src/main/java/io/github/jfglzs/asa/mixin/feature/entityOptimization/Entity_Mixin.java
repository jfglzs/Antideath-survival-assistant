package io.github.jfglzs.asa.mixin.feature.entityOptimization;

import io.github.jfglzs.asa.accessor.EntityAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public class Entity_Mixin implements EntityAccessor {
    @Unique
    private boolean asa$shouldTick = true;

    @Override
    public void asa$setShouldTick(boolean value) {
        this.asa$shouldTick = value;
    }

    @Override
    public boolean asa$shouldTick() {
        return this.asa$shouldTick;
    }
}
