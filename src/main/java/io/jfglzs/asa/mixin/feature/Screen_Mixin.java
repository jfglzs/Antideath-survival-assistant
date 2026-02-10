package io.jfglzs.asa.mixin.feature;

import io.jfglzs.asa.AsaMod;
import io.jfglzs.asa.utils.PlayerUtils;
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

import static io.jfglzs.asa.config.Configs.*;
import static io.jfglzs.asa.feature.materialrecycle.MaterialRecycler.*;
import static io.jfglzs.asa.utils.MCUtils.getMinecraftClient;

@Mixin(Screen.class)
public abstract class Screen_Mixin {
    @Inject(
            method = "init*",
            at = @At("TAIL")
    )
    private void initInject(CallbackInfo ci) {
        if (MATERIAL_RECYCLER.getBooleanValue()) {
            int maxClickCount = openedBoxSlot;
            int clickCount = 0;
            boolean allowCloseScreen = AsaMod.shouldOpenBox(false);
            List<Integer> Boxlist = PlayerUtils.getUnFullBoxIndexes(PlayerUtils.getAllBoxIndexes(35));

            MinecraftClient client = getMinecraftClient();
            if (client.player == null || client.interactionManager == null || openedBoxSlot == -1) return;
            ScreenHandler handler = client.player.currentScreenHandler;
            if (handler == null) return;
            if (!(handler instanceof ShulkerBoxScreenHandler)) return;

            for (Slot slot : handler.slots) {
                if (slot.inventory == client.player.getInventory() && maxClickCount != clickCount && !slot.getStack().isEmpty()) {
                    if (ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue() && !isBlackListed(slot.getStack().getItem())) {
                        client.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, client.player);
                        clickCount++;
                    } else if (isWhiteListed(slot.getStack().getItem()) && !ENABLE_MATERIAL_RECYCLER_BLACK_LIST.getBooleanValue()) {
                        client.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, client.player);
                        clickCount++;
                    }
                }
            }

            allowUpdate = true;
            if (allowCloseScreen && !Boxlist.isEmpty()) client.setScreen(null);
        }
    }
}