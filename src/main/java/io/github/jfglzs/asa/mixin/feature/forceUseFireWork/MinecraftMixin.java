package io.github.jfglzs.asa.mixin.feature.forceUseFireWork;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;

    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "startUseItem",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/InteractionHand;values()[Lnet/minecraft/world/InteractionHand;"),
            cancellable = true)
    private void forceUseFireWork(CallbackInfo ci)
    {
        if(player==null  || gameMode == null || !Configs.FORCE_USE_FIREWORK.getBooleanValue()){
            return;
        }

        InteractionHand hand = null;
        if(PlayerUtils.getPlayerHandStack(InteractionHand.OFF_HAND).is(Items.FIREWORK_ROCKET)){
            hand=InteractionHand.OFF_HAND;
        }
        if(PlayerUtils.getPlayerHandStack(InteractionHand.MAIN_HAND).is(Items.FIREWORK_ROCKET)){
           hand=InteractionHand.MAIN_HAND;
        }
        if(hand == null) return;
        //? >1.21.1{
        if ( gameMode.useItem(player, hand) instanceof InteractionResult.Success success) {
            if (success.swingSource() == InteractionResult.SwingSource.CLIENT) {
                this.player.swing(hand);
            }
            this.gameRenderer.itemInHandRenderer.itemUsed(hand);
            ci.cancel();
        }
        //?}else{
        /*var interactionResult3 = gameMode.useItem(player, hand);
        if (interactionResult3.consumesAction()) {
            if (interactionResult3.shouldSwing()) {
                this.player.swing(hand);
            }

            this.gameRenderer.itemInHandRenderer.itemUsed(hand);
            ci.cancel();
        }
        *///?}



    }

}
