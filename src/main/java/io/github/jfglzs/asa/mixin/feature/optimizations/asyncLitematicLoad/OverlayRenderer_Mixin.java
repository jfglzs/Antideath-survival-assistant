package io.github.jfglzs.asa.mixin.feature.optimizations.asyncLitematicLoad;

import com.google.common.collect.ImmutableMap;
import fi.dy.masa.litematica.render.OverlayRenderer;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.selection.Box;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(OverlayRenderer.class)
public class OverlayRenderer_Mixin {
    @Mutable
    @Shadow
    @Final
    private Map<SchematicPlacement, ImmutableMap<String, Box>> placements;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void OverlayRenderer(CallbackInfo ci) {
        this.placements = new ConcurrentHashMap<>();
    }
}
