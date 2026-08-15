package io.github.jfglzs.asa.mixin.feature.optItemModel;
//? if >= 26.1 {
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;
//?}

import org.spongepowered.asm.mixin.Mixin;
import io.github.jfglzs.asa.accessor.LayerRenderStateAccessor;

//~ if >= 26.1 'io.github.jfglzs.asa.utils.DummyClass' -> 'ItemStackRenderState.LayerRenderState' {
@Mixin(ItemStackRenderState.LayerRenderState.class)
//~}
public class LayerRenderState_Mixin implements LayerRenderStateAccessor {
    //? if >= 26.1 {
    @Mutable
    @Shadow
    @Final
    private List<BakedQuad> quads;

    @Override
    public void asa$setQuads(List<BakedQuad> quads) {
        this.quads = quads;
    }
    //?}
}
