package io.github.jfglzs.asa.mixin.feature.disableContainerBackGround;

//? if < 1.21.10 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.injection.At;
*///?}
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreen_Mixin {
    //? if < 1.21.10 {
    /*@WrapOperation(
            method = "renderBackground",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderTransparentBackground(Lnet/minecraft/client/gui/GuiGraphics;)V"
            )
    )
    public void renderBackground(AbstractContainerScreen instance, GuiGraphics guiGraphics, Operation<Void> original) {
    }
    *///?}
}
