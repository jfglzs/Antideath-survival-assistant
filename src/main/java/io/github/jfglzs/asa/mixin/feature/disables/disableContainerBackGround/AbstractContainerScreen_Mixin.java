package io.github.jfglzs.asa.mixin.feature.disables.disableContainerBackGround;

//? if < 1.21.10 {
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import net.minecraft.client.gui.GuiGraphics;
//import org.spongepowered.asm.mixin.injection.At;
//import io.github.jfglzs.asa.config.Configs;
//import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
//import org.spongepowered.asm.mixin.Mixin;
//
//@Mixin(AbstractContainerScreen.class)
//public class AbstractContainerScreen_Mixin {
//    @WrapOperation(
//            method = "renderBackground",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderTransparentBackground(Lnet/minecraft/client/gui/GuiGraphics;)V"
//            )
//    )
//    public void renderBackground(AbstractContainerScreen instance, GuiGraphics guiGraphics, Operation<Void> original) {
//        if (!Configs.Disables.DISABLE_CONTAINER_BACKGROUND.getBooleanValue()) original.call(instance, guiGraphics);
//    }
//
//}
//?} else {
@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.asa.utils.DummyClass.class)
public class AbstractContainerScreen_Mixin {
}
//?}
