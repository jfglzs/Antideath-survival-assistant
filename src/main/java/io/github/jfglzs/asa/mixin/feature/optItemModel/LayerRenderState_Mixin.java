package io.github.jfglzs.asa.mixin.feature.optItemModel;
//? if >= 26.1 {
import io.github.jfglzs.asa.accessor.LayerRenderStateAccessor;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public class LayerRenderState_Mixin implements LayerRenderStateAccessor {
    @Mutable
    @Shadow
    @Final
    private List<BakedQuad> quads;

    @Override
    public void asa$setQuads(List<BakedQuad> quads) {
        this.quads = quads;
    }
}
//?}
