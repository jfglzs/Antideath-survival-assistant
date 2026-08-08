package io.github.jfglzs.asa.mixin.event.screenEvents;

import io.github.jfglzs.asa.events.OpenScreenEvent;
import io.github.jfglzs.asa.feature.boxRestock.BoxRestockMannager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class Minecraft_Mixin {
    @Inject(
            method = {"setScreen", "setScreenAndShow"},
            at = @At("TAIL"),
            cancellable = true
    )
    private void setScreen_1(Screen screen, CallbackInfo ci) {
        if (screen instanceof ShulkerBoxScreen & BoxRestockMannager.context != null) {
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
