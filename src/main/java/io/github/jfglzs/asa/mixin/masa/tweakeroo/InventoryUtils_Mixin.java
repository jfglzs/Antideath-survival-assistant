package io.github.jfglzs.asa.mixin.masa.tweakeroo;

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
    @Unique
    private static final RateLimiter LIMITER = RateLimiter.create(0.5);

    @Inject(
            method = "preRestockHand",
            at = @At(value = "TAIL")
    )
    private static void preRestockHand(
            Player player,
            InteractionHand hand,
            boolean allowHotbar,
            CallbackInfo ci,
            @Local(name = "threshold") int threshold,
            @Local(name = "stackHand") ItemStack stackHand
            ) {
        if (Configs.AUTO_BOX_RESTROKE.getBooleanValue() && stackHand.getCount() < threshold) {
            if (stackHand.isEmpty() || stackHand.getMaxStackSize() == 1 || ! LIMITER.tryAcquire()) return;
            if (ShulkerUtils.findBoxToOpen(stackHand)) {
                BoxRestockMannager.context = new BoxRestockMannager.BoxRestockContext(stackHand, hand);
            }
        }
    }
}
