package io.github.jfglzs.asa.mixin.feature.disables.disablePlayerArmorRendener;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayer_Mixin {
    //? if >= 1.21.10 {
    @Inject(
            method = "renderArmorPiece",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderArmorPiece(CallbackInfo ci,
                                  @Local net.minecraft.client.renderer.entity.state.HumanoidRenderState state,
                                  @Local ItemStack stack) {
        //~ if >= 26.2 'EntityType' -> 'EntityTypes' {
        boolean bl = state.entityType == net.minecraft.world.entity.EntityType.PLAYER;
        //~}
        if (Configs.DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue() && bl && ! stack.is(Items.ELYTRA)) {
            ci.cancel();
        }
    }
    //?} else if >= 1.21.4 {
    //    @Inject(
    //            method = "render",
    //            at = @At("HEAD"),
    //            cancellable = true
    //    )
    //    private void render(CallbackInfo ci, @Local net.minecraft.client.renderer.entity.state.HumanoidRenderState state) {
    //        if (state instanceof net.minecraft.client.renderer.entity.state.PlayerRenderState && Configs.DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue()) {
    //            ci.cancel();
    //        }
    //    }
    //?} else if = 1.21.1 {
    //    @Inject(
    //            method = "renderArmorPiece",
    //            at = @At("HEAD"),
    //            cancellable = true
    //    )
    //    private void renderArmorInject(CallbackInfo ci, @Local net.minecraft.world.entity.LivingEntity entity, @Local net.minecraft.world.entity.EquipmentSlot slot) {
    //        if(entity instanceof net.minecraft.world.entity.player.Player p && Configs.DISABLE_PLAYER_ARMOR_RENDER.getBooleanValue()) {
    //            if (slot == net.minecraft.world.entity.EquipmentSlot.CHEST && p.getInventory().getItem(38).is(Items.ELYTRA)) {
    //                return;
    //            }
    //            ci.cancel();
    //        }
    //    }
    //?}
}
