package io.github.jfglzs.asa.mixin.feature.lms;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreen_Mixin extends Screen {
    protected HandledScreen_Mixin(Text title) {
        super(title);
    }

    @Inject(
            method = "keyPressed",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;close()V")
    )
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (((HandledScreen) (Object) this) instanceof CreativeInventoryScreen) {
            Configs.lockCreativeScreen = false;
        }
    }
}
