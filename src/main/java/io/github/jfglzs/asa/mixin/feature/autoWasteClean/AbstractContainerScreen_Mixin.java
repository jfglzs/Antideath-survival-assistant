package io.github.jfglzs.asa.mixin.feature.autoWasteClean;

import io.github.jfglzs.asa.feature.autoWasteClean.AutoWasteCleanProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class AbstractContainerScreen_Mixin {
    @Inject(
            method = {"setScreen", "setScreenAndShow"},
            at = @At("TAIL")
    )
    private void setScreen(Screen screen, CallbackInfo ci) {
        if (screen instanceof AbstractContainerScreen<?> containerScreen) {
            AutoWasteCleanProcessor.process(containerScreen);
        }
    }
}
