package io.github.jfglzs.asa.mixin.feature;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
//? if > 1.21.1
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
//? if > 1.21.1
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.asa.config.Configs.DISABLE_PLAYER_ARMOR_RENDER;
@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayer_Mixin {
    //? if > 1.21.1 {
    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void render_Inject(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, HumanoidRenderState state, float f, float g, CallbackInfo ci) {
        if (state instanceof PlayerRenderState && DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue()) {
            ci.cancel();
        }
    }
    //?} else {
        /*@Inject(method = "renderArmorPiece",
                at = @At("HEAD"),
                cancellable = true
        )
        private void renderArmorInject(PoseStack matrices, MultiBufferSource vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot, int light, HumanoidModel<?> model, CallbackInfo ci) {
            if(entity instanceof Player p && DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue()) {
                if (armorSlot.getName().equals("chest") && p.getInventory().getItem(38).getItem().equals(Items.ELYTRA)) {
                    return;
                }
                ci.cancel();
            }
        }
    *///?}
}
