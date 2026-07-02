package io.github.jfglzs.asa.mixin.feature.transparentItemFrame;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;

//? if <= 1.21.3 {

/*import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.item.ItemDisplayContext;

*///?}

//? if = 1.21.1 {

/*import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.client.Minecraft;

*///?}
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >= 26.2
//import org.objectweb.asm.Opcodes;

//? if >= 26.1 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} else if < 1.21.10 {

/*import net.minecraft.client.renderer.MultiBufferSource;

*///?} else {

/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;

*///?}

//? if != 1.21.1 {
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.client.renderer.MapRenderer;
//?}

//? if != 1.21.1 && < 26.1 {

/*import org.objectweb.asm.Opcodes;

*///?}

@Mixin(value = ItemFrameRenderer.class, priority = 900)
public abstract class ItemFrameRenderer_Mixin {

    //? if > 1.21.3 {
    @Shadow
    protected abstract int getLightCoords(boolean isGlowFrame, int glowLight, int normalLight);
    //?}

    //? if >= 1.21.3 {
    @Shadow
    @Final
    private MapRenderer mapRenderer;
    //?}

    //? if = 1.21.3 {
    
    /*@Shadow
    protected abstract int getLightVal(boolean par1, int par2, int par3);
    
    *///?} else if = 1.21.1 {
    
    /*@Shadow
    protected abstract int getLightVal(ItemFrame entity, int par2, int par3);
    
    *///?}

    //? if <= 1.21.3 {
    
    /*@Shadow
    @Final
    private ItemRenderer itemRenderer;
    
    *///?}

    @Inject(
            //? if = 1.21.1 {
            
            /*method = "render(Lnet/minecraft/world/entity/decoration/ItemFrame;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            
            *///?} else if < 1.21.10 {
            
            /*method = "render(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            
            *///?} else if >= 26.1 {
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            //?} else {
            
            /*method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            
            *///?}

            //? if = 26.1 {
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/BlockModelRenderState;isEmpty()Z"
            ),
            //?} else if != 1.21.1 && < 26.1 {
            
            /*at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;isInvisible:Z",
                    ordinal = 0,
                    opcode = Opcodes.GETFIELD
            ),
            
            *///?} else if >= 26.2 {
            /*at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;isInvisible:Z",
                    opcode = Opcodes.GETFIELD
            ),

            *///?} else {
            /*at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/ItemFrame;getItem()Lnet/minecraft/world/item/ItemStack;",
                    shift = At.Shift.AFTER
            ),
            *///?}

            cancellable = true
    )

            //? if = 1.21.1 {
    
    /*private void render(ItemFrame entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
    
            *///?} else if < 1.21.10 {
    
    /*private void render(ItemFrameRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
    
            *///?} else if >= 1.21.10 && < 26.2 {
    private void render(ItemFrameRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
            //?} else {
    /*private void render(ItemFrameRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
            *///?}

        if (!Configs.TRANSPARENT_ITEM_FRAME.getBooleanValue()) {
            return;
        }

        //? if < 1.21.10 && > 1.21.3 {
        
        /*poseStack.translate(0.0F, 0.0F, 0.4375F);

        if (renderState.mapId != null) {
            int i = renderState.rotation % 4 * 2;
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) i * 360.0F / 8.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

            float h = 0.0078125F;
            poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
            poseStack.translate(-64.0F, -64.0F, 0.0F);
            poseStack.translate(0.0F, 0.0F, -1.0F);

            int j = this.getLightCoords(renderState.isGlowFrame, 15728850, packedLight);
            this.mapRenderer.render(renderState.mapRenderState, poseStack, bufferSource, true, j);
        }
        else if (!renderState.item.isEmpty()) {
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) renderState.rotation * 360.0F / 8.0F));

            int i = this.getLightCoords(renderState.isGlowFrame, 15728880, packedLight);
            poseStack.scale(0.5F, 0.5F, 0.5F);

            renderState.item.render(poseStack, bufferSource, i, OverlayTexture.NO_OVERLAY);
        }
        
        *///?} else if >= 26.1 {

        poseStack.translate(0.0F, 0.0F, 0.4375F);

        if (renderState.mapId != null) {
            int rotation = renderState.rotation % 4 * 2;

            poseStack.mulPose(Axis.ZP.rotationDegrees((float) rotation * 360.0F / 8.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

            float s = 0.0078125F;
            poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);

            poseStack.translate(-64.0F, -64.0F, 0.0F);
            poseStack.translate(0.0F, 0.0F, -1.0F);

            int lightCoords = this.getLightCoords(
                    renderState.isGlowFrame,
                    15728850,
                    renderState.lightCoords
            );

            this.mapRenderer.render(
                    renderState.mapRenderState,
                    poseStack,
                    submitNodeCollector,
                    true,
                    lightCoords
            );
        }
        else if (!renderState.item.isEmpty()) {
            poseStack.mulPose(
                    Axis.ZP.rotationDegrees((float) renderState.rotation * 360.0F / 8.0F)
            );

            int lightVal = this.getLightCoords(
                    renderState.isGlowFrame,
                    15728880,
                    renderState.lightCoords
            );

            poseStack.scale(0.5F, 0.5F, 0.5F);

            renderState.item.submit(
                    poseStack,
                    submitNodeCollector,
                    lightVal,
                    OverlayTexture.NO_OVERLAY,
                    renderState.outlineColor
            );
        }

        //?} else if >= 1.21.10 && < 26.1 {
        
        /*poseStack.translate(0.0F, 0.0F, 0.4375F);

        if (renderState.mapId != null) {
            int i = renderState.rotation % 4 * 2;
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) i * 360.0F / 8.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

            float h = 0.0078125F;
            poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);

            poseStack.translate(-64.0F, -64.0F, 0.0F);
            poseStack.translate(0.0F, 0.0F, -1.0F);

            int j = this.getLightCoords(renderState.isGlowFrame, 15728850, renderState.lightCoords);
            this.mapRenderer.render(renderState.mapRenderState, poseStack, submitNodeCollector, true, j);
        }
        else if (!renderState.item.isEmpty()) {
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) renderState.rotation * 360.0F / 8.0F));

            int i = this.getLightCoords(renderState.isGlowFrame, 15728880, renderState.lightCoords);
            poseStack.scale(0.5F, 0.5F, 0.5F);

            renderState.item.submit(
                    poseStack,
                    submitNodeCollector,
                    i,
                    OverlayTexture.NO_OVERLAY,
                    renderState.outlineColor
            );
        }
        
        *///?} else if != 1.21.1 {
        
        /*if (!renderState.itemStack.isEmpty()) {
            MapId mapId = renderState.mapId;

            if (renderState.isInvisible) {
                poseStack.translate(0.0F, 0.0F, 0.5F);
            } else {
                poseStack.translate(0.0F, 0.0F, 0.4375F);
            }

            int i = mapId != null ? renderState.rotation % 4 * 2 : renderState.rotation;
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) i * 360.0F / 8.0F));

            if (mapId != null) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

                float h = 0.0078125F;
                poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);

                poseStack.translate(-64.0F, -64.0F, 0.0F);
                poseStack.translate(0.0F, 0.0F, -1.0F);

                int j = this.getLightVal(renderState.isGlowFrame, 15728850, packedLight);
                this.mapRenderer.render(renderState.mapRenderState, poseStack, bufferSource, true, j);
            }
            else if (renderState.itemModel != null) {
                int k = this.getLightVal(renderState.isGlowFrame, 15728880, packedLight);

                poseStack.scale(0.5F, 0.5F, 0.5F);

                this.itemRenderer.render(
                        renderState.itemStack,
                        ItemDisplayContext.FIXED,
                        false,
                        poseStack,
                        bufferSource,
                        k,
                        OverlayTexture.NO_OVERLAY,
                        renderState.itemModel
                );
            }
        }
        
        *///?} else {
        
        /*var itemStack = entity.getItem();

        if (!itemStack.isEmpty()) {
            MapId mapId = entity.getFramedMapId(itemStack);

            poseStack.translate(0.0F, 0.0F, 0.4375F);

            int i = mapId != null ? entity.getRotation() % 4 * 2 : entity.getRotation();
            poseStack.mulPose(Axis.ZP.rotationDegrees(i * 360.0F / 8.0F));

            if (mapId != null) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

                float f = 0.0078125F;
                poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);

                poseStack.translate(-64.0F, -64.0F, 0.0F);

                MapItemSavedData mapItemSavedData = MapItem.getSavedData(mapId, entity.level());

                poseStack.translate(0.0F, 0.0F, -1.0F);

                if (mapItemSavedData != null) {
                    int j = this.getLightVal(entity, 15728850, packedLight);

                    Minecraft.getInstance()
                            .gameRenderer
                            .getMapRenderer()
                            .render(poseStack, buffer, mapId, mapItemSavedData, true, j);
                }
            }
            else {
                int k = this.getLightVal(entity, 15728880, packedLight);

                poseStack.scale(0.5F, 0.5F, 0.5F);

                this.itemRenderer.renderStatic(
                        itemStack,
                        ItemDisplayContext.FIXED,
                        k,
                        OverlayTexture.NO_OVERLAY,
                        poseStack,
                        buffer,
                        entity.level(),
                        entity.getId()
                );
            }
        }
        
        *///?}

        poseStack.popPose();
        ci.cancel();
    }
}