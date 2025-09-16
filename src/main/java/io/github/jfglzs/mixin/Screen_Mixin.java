package io.github.jfglzs.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.config.Configs.ENABLE_MATERIAL_RECYCLER_BLACK_LIST;
import static io.github.jfglzs.config.Configs.MATERIAL_RECYCLER;
import static io.github.jfglzs.feature.materialrecycle.MaterialRecycler.*;
import static io.github.jfglzs.utils.MCUtils.getWorld;

@Mixin(Screen.class)
public abstract class Screen_Mixin
{
    @Inject(method = "init*", at = @At("TAIL"))
        private void onInit (CallbackInfo ci)
    {
        if (MATERIAL_RECYCLER.getBooleanValue())
        {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.interactionManager == null) return;

            ScreenHandler handler = client.player.currentScreenHandler;
            if (handler == null) return;
            if (handler instanceof PlayerScreenHandler) return;
            if (!(handler instanceof ShulkerBoxScreenHandler)) return;

            for (Slot slot : handler.slots)
            {
                if (ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue() && !isBlackListed(slot.getStack().getItem()))
                {
                    if (slot.inventory == client.player.getInventory() && !slot.getStack().isEmpty())
                    {
                        client.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, client.player);
//                        client.player.closeHandledScreen();
                        allowUpdate = true;
                    }

                } else if (isWhiteListed(slot.getStack().getItem()))
                {
                    if (slot.inventory == client.player.getInventory() && !slot.getStack().isEmpty())
                    {
                        client.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, client.player);
                        client.player.closeHandledScreen();
                        allowUpdate = true;
                    }
                }
            }
            client.player.closeHandledScreen();
        }
    }
}