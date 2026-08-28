package io.github.jfglzs.asa.mixin.feature.optimizations.optItemModel;
//? if >= 26.1 {

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
//?}

import org.spongepowered.asm.mixin.Mixin;
import io.github.jfglzs.asa.accessor.ILayerRenderStateAccessor;
import org.spongepowered.asm.mixin.injection.At;

//~ if >= 26.1 'io.github.jfglzs.asa.utils.DummyClass' -> 'ItemStackRenderState.LayerRenderState' {
@Mixin(ItemStackRenderState.LayerRenderState.class)
//~}
public class LayerRenderState_Mixin implements ILayerRenderStateAccessor {
    //? if >= 26.1 {
    @Mutable
    @Shadow
    @Final
    private List<BakedQuad> quads;

    @Override
    public void asa$setQuads(List<BakedQuad> quads) {
        this.quads = quads;
    }

    @WrapOperation(
            method = "clear",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;clear()V"
            )
    )
    public void clear(List<BakedQuad> instance, Operation<Void> original) {
        if (Configs.OPT_ITEM_MODEL.getBooleanValue()) {
            this.quads = null;
            return;
        }
        original.call(instance);
    }
    //?}
}
