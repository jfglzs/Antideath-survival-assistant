package io.github.jfglzs.asa.accessor;
//? if >= 26.1 {

import net.minecraft.client.resources.model.geometry.BakedQuad;

import java.util.List;
//?}

public interface LayerRenderStateAccessor {
    //? if >= 26.1 {
    void asa$setQuads(List<BakedQuad> quads);
    //?}
}
