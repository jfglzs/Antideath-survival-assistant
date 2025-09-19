package io.github.jfglzs.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
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
import static io.github.jfglzs.utils.MCUtils.getMinecraftClient;

//TODO 修复材料回收助手黑名单功能
@Mixin(Screen.class)
public abstract class Screen_Mixin
{
    @Inject(method = "init*", at = @At("TAIL"))
    private void onInit (CallbackInfo ci)
    {
        int maxClickCount;
        int clickCount = 0;

        if (MATERIAL_RECYCLER.getBooleanValue())
        {
            MinecraftClient client = getMinecraftClient();
            if (client.player == null || client.interactionManager == null) return;

            ScreenHandler handler = client.player.currentScreenHandler;
            if (handler == null) return;
            if (!(handler instanceof ShulkerBoxScreenHandler)) return;

            if (openedBoxSlot == -1) return;

            maxClickCount = openedBoxSlot;

            for (Slot slot : handler.slots)
            {
                if (ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue() && !isBlackListed(slot.getStack().getItem()))
                {
                    if (slot.inventory == client.player.getInventory() && !slot.getStack().isEmpty() && maxClickCount != clickCount)
                    {
                        client.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, client.player);
                        clickCount++;
                    }
                }
                else if (isWhiteListed(slot.getStack().getItem()) && slot.inventory == client.player.getInventory() && !slot.getStack().isEmpty() && maxClickCount != clickCount && !ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue())
                {
                    client.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, client.player);
                    clickCount++;
                }
            }

            allowUpdate = true;
            client.setScreen(null);
        }
    }

}