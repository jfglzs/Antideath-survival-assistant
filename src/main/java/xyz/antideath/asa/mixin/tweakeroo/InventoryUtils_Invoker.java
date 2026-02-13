package xyz.antideath.asa.mixin.tweakeroo;

import fi.dy.masa.tweakeroo.util.InventoryUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InventoryUtils.class)
public interface InventoryUtils_Invoker {
    @Invoker("swapItemToHand")
    static void swapItemToHand(PlayerEntity player, Hand hand, int slotNumber) {}
}
