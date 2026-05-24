package io.github.jfglzs.asa.mixin.tweakeroo;

import fi.dy.masa.tweakeroo.util.InventoryUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InventoryUtils.class)
public interface InventoryUtils_Invoker {
    @Invoker("swapItemToHand")
    static void swapItemToHand(Player player, InteractionHand hand, int slotNumber) {}
}
