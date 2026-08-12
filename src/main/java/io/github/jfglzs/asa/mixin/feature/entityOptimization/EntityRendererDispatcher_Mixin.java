package io.github.jfglzs.asa.mixin.feature.entityOptimization;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.accessor.EntityAccessor;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererDispatcher_Mixin {
    @WrapMethod(
            method = "shouldRender"
    )
    private <E extends Entity> boolean shouldRender(E entity, Frustum culler, double camX, double camY, double camZ, Operation<Boolean> original) {
        boolean bl = original.call(entity, culler, camX, camY, camZ);
        ((EntityAccessor) entity).asa$setShouldTick(bl);
        return bl;
    }
}
