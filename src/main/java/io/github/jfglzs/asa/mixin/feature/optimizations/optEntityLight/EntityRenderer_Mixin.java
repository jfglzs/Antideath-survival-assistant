package io.github.jfglzs.asa.mixin.feature.optimizations.optEntityLight;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRenderer_Mixin {
    @Inject(
            method = "getPackedLightCoords",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getPackedLightCoords(Entity entity, float partialTickTime, CallbackInfoReturnable<Integer> cir) {
        if (Configs.Optimizations.OPT_ENTITY_LIGHT.getBooleanValue())
            cir.setReturnValue(128);
    }
}
