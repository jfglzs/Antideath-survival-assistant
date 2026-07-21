package io.github.jfglzs.asa.mixin.tweakeroo;

import com.google.common.util.concurrent.RateLimiter;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.tweakeroo.util.InventoryUtils;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.boxRestock.BoxRestockMannager;
import io.github.jfglzs.asa.utils.PlayerUtils;
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
    private static RateLimiter limiter = RateLimiter.create(1);

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
        if (hand != InteractionHand.MAIN_HAND) return;

        ItemStack currentHandStack = player.getItemInHand(hand);
        if (BoxRestockMannager.canStartAutoRestock(currentHandStack, threshold)) {
            for (int index : PlayerUtils.getAllBoxIndexes(36)) {
                ItemStack boxStack = player.inventoryMenu.getSlot(index).getItem();
                if (boxStack.isEmpty() || currentHandStack.isEmpty() || currentHandStack.getMaxStackSize() == 1) return;
                for (ItemStack itemStack : PlayerUtils.getBoxItemStacks(boxStack)) {
                    if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(itemStack, currentHandStack)) {
                        if (limiter.tryAcquire() && BoxRestockMannager.startAutoRestock(currentHandStack, index, threshold)) {
                            System.out.println("[ASA BoxRestock] opening shulker slot=" + index + " item=" + currentHandStack.getItem() + " count=" + currentHandStack.getCount());
                            ShulkerUtils.open(index);
                            return;
                        }
                    }
                }
            }
        }
    }
}
