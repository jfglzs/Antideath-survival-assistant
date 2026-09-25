package io.github.jfglzs.asa.mixin.feature.optimizations.optItemFrame;

//? if >= 1.21.8 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;

//? if >= 26.1 {
import io.github.jfglzs.asa.config.options.ItemFrameVisibility;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.BlockModelRenderState;
//?}

@Mixin(ItemFrameRenderer.class)
public class ItemFrameRenderer_Mixin {
    //? if >= 26.1 {
    @WrapOperation(
            method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/BlockModelResolver;updateForItemFrame(Lnet/minecraft/client/renderer/block/BlockModelRenderState;ZZ)V"
            )
    )
    public void updateForItemFrame(BlockModelResolver instance, BlockModelRenderState renderState, boolean isGlowing,
                                   boolean map, Operation<Void> original, @Local ItemStack stack) {
        if (Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue()) {
            var visibility = Configs.Optimizations.ITEM_FRAME_VISIBILITY.getOptionListValue();
            if (visibility == ItemFrameVisibility.EMPTY_ONLY) {
                if (! stack.isEmpty())
                    return;
            }
            else if (visibility == ItemFrameVisibility.ALWAYS_INVISIBLE) {
                return;
            }
        }
        original.call(instance, renderState, isGlowing, map);
    }

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    //? if < 26.3 {
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    //?} else {
                    /*target = "Lcom/mojang/blaze3d/vertex/PoseStack;rotateDegrees(Lcom/mojang/math/Axis;F)V",
                    *///?}
                    ordinal = 2
            )
    )
    //? if < 26.3 {
    public void submit(PoseStack instance, org.joml.Quaternionfc by, Operation<Void> original, @Local ItemFrameRenderState state) {
    //?} else {
    /*public void submit(PoseStack instance, com.mojang.math.Axis axis, float angle, Operation<Void> original, @Local ItemFrameRenderState state) {
    *///?}
        if (state.rotation != 0 && Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue())
            //? if < 26.3 {
            original.call(instance, by);
            //?} else {
            /*original.call(instance, axis, angle);
            *///?}
    }

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    //? if < 26.3 {
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    //?} else {
                    /*target = "Lcom/mojang/blaze3d/vertex/PoseStack;rotateDegrees(Lcom/mojang/math/Axis;F)V",
                    *///?}
                    ordinal = 4
            )
    )
    //? if < 26.3 {
    public void submit_1(PoseStack instance, org.joml.Quaternionfc by, Operation<Void> original, @Local ItemFrameRenderState state) {
    //?} else {
    /*public void submit_1(PoseStack instance, com.mojang.math.Axis axis, float angle, Operation<Void> original, @Local ItemFrameRenderState state) {
    *///?}
        if (state.rotation != 0 && Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue())
            //? if < 26.3 {
            original.call(instance, by);
            //?} else {
            /*original.call(instance, axis, angle);
            *///?}
    }
    //?}

    //? if >= 1.21.8 {
    @WrapOperation(
            method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/item/ItemModelResolver;updateForNonLiving(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/Entity;)V"
            )
    )
    public void updateForNonLiving(ItemModelResolver instance, ItemStackRenderState output, ItemStack item,
                                   ItemDisplayContext displayContext, Entity entity, Operation<Void> original) {
        if (Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue() && item.is(Items.FILLED_MAP))
            return;

        original.call(instance, output, item, displayContext, entity);
    }
    //?}
}
//?} else {
//@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.asa.utils.DummyClass.class)
//public class ItemFrameRenderer_Mixin {
//}
//?}