package io.github.jfglzs.asa.mixin.event.openScreenEvent;

import io.github.jfglzs.asa.events.OpenScreenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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
        OpenScreenEvent.INSTANCE.update(screen);
    }
}
