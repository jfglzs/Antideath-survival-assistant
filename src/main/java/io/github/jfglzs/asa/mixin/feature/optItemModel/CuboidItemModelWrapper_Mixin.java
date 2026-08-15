package io.github.jfglzs.asa.mixin.feature.optItemModel;
//? if >= 26.1 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.asa.accessor.LayerRenderStateAccessor;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;
//?}

//~ if >= 26.1 'io.github.jfglzs.asa.utils.DummyClass' -> 'CuboidItemModelWrapper' {
import org.spongepowered.asm.mixin.Mixin;
@Mixin(CuboidItemModelWrapper.class)
//~}
public class CuboidItemModelWrapper_Mixin {
    //? if >= 26.1 {
    @Shadow
    @Final
    private QuadCollection quads;

    @WrapOperation(
            method = "update",
            at = @At(value = "INVOKE", target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z")
    )
    private boolean addAll(List instance, Collection<Object> es, Operation<Boolean> original) {
        return ! Configs.OPT_ITEM_MODEL.getBooleanValue() && original.call(instance, es);
    }

    @Inject(
            method = "update",
            at = @At(value = "INVOKE", target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z")
    )
    private void addAll(CallbackInfo ci, @Local ItemStackRenderState.LayerRenderState state) {
        if (Configs.OPT_ITEM_MODEL.getBooleanValue()) {
            ((LayerRenderStateAccessor) state).asa$setQuads(this.quads.getAll());
        }
    }
    //?}
}
