package io.github.jfglzs.asa.mixin.tweakeroo;

import fi.dy.masa.tweakeroo.util.InventoryUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryUtils.class)
public class InventoryUtils_Mixin {
    @Inject(
            method = "preRestockHand",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickSlot(IIILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
    )
    private static void preRestockHand(PlayerEntity player, Hand hand, boolean allowHotbar, CallbackInfo ci) {
    }
}
