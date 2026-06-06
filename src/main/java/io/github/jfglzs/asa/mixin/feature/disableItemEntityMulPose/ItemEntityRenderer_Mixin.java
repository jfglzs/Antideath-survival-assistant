package io.github.jfglzs.asa.mixin.feature.disableItemEntityMulPose;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
//? if >= 1.21.4 {
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
//?} else {
/*import org.joml.Quaternionf;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRenderer_Mixin {
    @WrapOperation(
            //? if >= 26.1 {
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            //?} else if = 1.21.1 {
            /*method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            *///?} else if < 1.21.10 {
            /*method = "render(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            *///?} else {
            /*method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            *///?}

            //? if >= 26.1 {
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V")
            //?} else if <= 1.21.4 {
            /*at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V")
            *///?} else {
            /*at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V")
            *///?}

    )
    //? if >= 26.1 {
    public void submit(PoseStack instance, Quaternionfc q, Operation<Void> original) {
    //?} else if <= 1.21.4 {
    /*public void submit(PoseStack instance, Quaternionf q, Operation<Void> original) {
    *///?} else  {
    /*public void submit(PoseStack instance, Quaternionfc q, Operation<Void> original) {
    *///?}
        if (!Configs.DISABLE_ITEM_ENTITY_MULPOSE.getBooleanValue()) original.call(instance, q);
    }
}
