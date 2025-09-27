package io.github.jfglzs.mixin.feature;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
//#if MC > 12101
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
//#else
//$$ import net.minecraft.client.render.entity.model.BipedEntityModel;
//$$ import net.minecraft.entity.LivingEntity;
//$$ import net.minecraft.entity.player.PlayerEntity;
//$$ import net.minecraft.entity.EquipmentSlot;
//#endif
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.config.Configs.DISABLE_PLAYER_ARMOR_RENDER;
import static net.minecraft.entity.EntityType.PLAYER;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRenderer_Mixin
{
    //#if MC > 12101

    @Inject(method = "renderArmor" , at = @At("HEAD"), cancellable = true)
    private void renderArmorInject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, EquipmentSlot slot, int light, BipedEntityModel armorModel, CallbackInfo ci)
    {
        if (stack.getItem().equals(Items.ELYTRA))
        {
            ci.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V" , at = @At("HEAD") , cancellable = true)
    public void renderInject(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, BipedEntityRenderState bipedEntityRenderState, float f, float g, CallbackInfo ci)
    {
        //#if MC > 12104
        if (bipedEntityRenderState.entityType.equals(PLAYER) && DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue())
        {
             ci.cancel();
        }
        //#else
        //$$        if (DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue())
        //$$        {
        //$$            ci.cancel();
        //$$        }
        //#endif
    }
    //#else
    //$$    @Inject(method = "renderArmor" , at = @At("HEAD") , cancellable = true)
    //$$    private void renderArmorInject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot, int light, BipedEntityModel<?> model, CallbackInfo ci)
    //$$    {
    //$$        if(entity instanceof PlayerEntity && DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue())
    //$$        {
    //$$            PlayerEntity PE = (PlayerEntity) entity;
    //$$            if (armorSlot.getName().equals("chest"))
    //$$            {
    //$$                if (!PE.getInventory().getStack(38).getItem().equals(Items.ELYTRA))
    //$$                {
    //$$                    ci.cancel();
    //$$                }
    //$$
    //$$            } else
    //$$            {
    //$$                ci.cancel();
    //$$           }
    //$$
    //$$        }
    //$$    }
    //#endif
}
