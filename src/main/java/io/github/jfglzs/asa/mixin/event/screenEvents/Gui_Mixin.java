package io.github.jfglzs.asa.mixin.event.screenEvents;

//? if >= 26.2 {
/*import io.github.jfglzs.asa.events.OpenScreenEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class Gui_Mixin {
    @Inject(
            method = "setScreen",
            at = @At("TAIL")
    )
    private void setScreen(Screen screen, CallbackInfo ci) {
        OpenScreenEvent.INSTANCE.update(screen);
    }
}
*///?} else {
@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.asa.utils.DummyClass.class)
public class Gui_Mixin {
}
//?}