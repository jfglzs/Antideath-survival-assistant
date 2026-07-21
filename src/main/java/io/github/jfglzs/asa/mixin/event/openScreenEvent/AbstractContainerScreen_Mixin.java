package io.github.jfglzs.asa.mixin.event.openScreenEvent;

import io.github.jfglzs.asa.events.OpenScreenEvent;
import io.github.jfglzs.asa.feature.boxRestock.BoxRestockMannager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class AbstractContainerScreen_Mixin {
    @Inject(
            method = {"setScreen", "setScreenAndShow"},
            at = @At("HEAD"),
            cancellable = true
    )
    private void hideBoxRestockScreen(Screen screen, CallbackInfo ci) {
        if (screen instanceof ShulkerBoxScreen && BoxRestockMannager.shouldSuppressShulkerScreen()) {
            System.out.println("[ASA BoxRestock] hiding shulker screen during auto restock");
            ci.cancel();
        }
    }

    @Inject(
            method = {"setScreen", "setScreenAndShow"},
            at = @At("TAIL")
    )
    private void setScreen(Screen screen, CallbackInfo ci) {
        OpenScreenEvent.INSTANCE.update(screen);
    }
}
