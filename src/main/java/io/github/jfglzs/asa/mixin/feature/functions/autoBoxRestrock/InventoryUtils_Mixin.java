package io.github.jfglzs.asa.mixin.feature.functions.autoBoxRestrock;

import com.google.common.util.concurrent.RateLimiter;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.tweakeroo.util.InventoryUtils;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.boxRestock.BoxRestockMannager;
import io.github.jfglzs.asa.utils.ShulkerUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryUtils.class)
public class InventoryUtils_Mixin {
    @Unique private static final RateLimiter LIMITER_MAIN = RateLimiter.create(0.3);
    @Unique private static final RateLimiter LIMITER_OFF = RateLimiter.create(0.3);

    @Inject(
            method = "preRestockHand",
            at = @At(value = "TAIL")
    )
    private static void preRestockHand(Player player, InteractionHand hand, boolean allowHotbar, CallbackInfo ci,
                                       @Local(name = "threshold") int threshold,
                                       @Local(name = "stackHand") ItemStack stack) {
        if (Configs.Functions.AUTO_BOX_RESTROKE.getBooleanValue() && stack.getCount() < threshold) {
            if (stack.isEmpty() || stack.getMaxStackSize() == 1 || ! LIMITER_MAIN.tryAcquire())
                return;
            tryRestrock(stack, hand);
        }
    }

    @Inject(
            method = "restockNewStackToHand",
            at = @At("TAIL")
    )
    private static void restockNewStackToHand(Player player, InteractionHand hand, ItemStack stack,
                                              boolean allowHotbar, CallbackInfo ci,
                                              @Local(ordinal = 0) int slotWithItem) {
        if (Configs.Functions.AUTO_BOX_RESTROKE.getBooleanValue() && slotWithItem == - 1) {
            if (stack.isEmpty() || ! LIMITER_OFF.tryAcquire())
                return;
            tryRestrock(stack, hand);
        }
    }

    @Unique
    private static void tryRestrock(ItemStack stack, InteractionHand hand) {
        if (ShulkerUtils.findBoxToOpen(stack)) {
            BoxRestockMannager.context = new BoxRestockMannager.BoxRestockContext(stack, hand);
        }
    }
}
