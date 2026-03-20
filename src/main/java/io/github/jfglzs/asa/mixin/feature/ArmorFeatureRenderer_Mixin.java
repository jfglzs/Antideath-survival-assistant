package io.github.jfglzs.asa.mixin.feature;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
//? > 1.20.1 {
// import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
//?} else {
/*import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
*///?}
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static io.github.jfglzs.asa.config.Configs.DISABLE_PLAYER_ARMOR_RENDER;
import static net.minecraft.entity.EntityType.PLAYER;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRenderer_Mixin {
    //? if > 1.20.1 {
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V" ,
            at = @At("HEAD"),
            cancellable = true
    )
    public void render_Inject(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, BipedEntityRenderState bipedEntityRenderState, float f, float g, CallbackInfo ci) {
        //? if > 1.21.4 {
        if (bipedEntityRenderState.entityType.equals(PLAYER) && DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue()) {
             ci.cancel();
        }
        //?} else {
                /*if (DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue()) {
                    ci.cancel();
                }
        *///?}
    }
    //?} else {
        /*@Inject(method = "renderArmor",
                at = @At("HEAD"),
                cancellable = true
        )
        private void renderArmorInject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot, int light, BipedEntityModel<?> model, CallbackInfo ci) {
            if(entity instanceof PlayerEntity p && DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue()) {
                if (armorSlot.getName().equals("chest") && !p.getInventory().getStack(38).getItem().equals(Items.ELYTRA)) {
                    ci.cancel();
                }
            }
        }
    *///?}
}
