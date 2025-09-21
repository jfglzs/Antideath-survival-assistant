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

import java.util.List;

import static io.github.jfglzs.AsaMod.shouldOpenBox;
import static io.github.jfglzs.config.Configs.*;
import static io.github.jfglzs.feature.materialrecycle.MaterialRecycler.*;
import static io.github.jfglzs.utils.FabricUtils.isModLoaded;
import static io.github.jfglzs.utils.MCUtils.getMinecraftClient;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.getAllBoxIndexes;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.getUnFullBoxIndexes;

@Mixin(Screen.class)
public abstract class Screen_Mixin
{
    @Inject(method = "init*", at = @At("TAIL"))
    private void initInject (CallbackInfo ci)
    {
        if (MATERIAL_RECYCLER.getBooleanValue() && MATERIAL_RECYCLER_AUTO.getBooleanValue() && isModLoaded("quickshulker"))
        {
            int maxClickCount = openedBoxSlot;
            int clickCount = 0;
            boolean allowCloseScreen = shouldOpenBox(false);
            List<Integer> Boxlist = getUnFullBoxIndexes(getAllBoxIndexes(35));

            MinecraftClient client = getMinecraftClient();
            if (client.player == null || client.interactionManager == null) return;
            ScreenHandler handler = client.player.currentScreenHandler;
            if (handler == null) return;
            if (!(handler instanceof ShulkerBoxScreenHandler)) return;
            if (openedBoxSlot == -1) return;

            for (Slot slot : handler.slots)
            {
                if (ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue() && !isBlackListed(slot.getStack().getItem()) && !slot.getStack().isEmpty())
                {
                    if (slot.inventory == client.player.getInventory() && maxClickCount != clickCount)
                    {
                        client.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, client.player);
//                        System.out.println(slot.getStack().getItem());
                        clickCount++;
                    }
                }
                else if (isWhiteListed(slot.getStack().getItem()) && !ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue())
                {
                    if (maxClickCount != clickCount && slot.inventory == client.player.getInventory() && !slot.getStack().isEmpty())
                    {
                        client.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, client.player);
                        clickCount++;
                    }

                }
            }

            allowUpdate = true;
//            System.out.println(allowCloseScreen);
            if (allowCloseScreen && !Boxlist.isEmpty()) client.setScreen(null);
        }
    }

}