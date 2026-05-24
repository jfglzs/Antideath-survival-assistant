package io.github.jfglzs.asa.mixin.feature;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlay_Mixin {
    @Inject(
            method = "drawGameInformation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;renderLines(Lnet/minecraft/client/gui/GuiGraphics;Ljava/util/List;Z)V"
            )
    )
    protected void drawLeftTextInject(GuiGraphics context, CallbackInfo ci , @Local List<String> list) {
        list.add(null);
        list.add(String.format("[ASA] Antideath Survival Assistant V %s", AsaMod.version));
        list.add(String.format("[ASA] %S Features loaded", Configs.getFeatureAmount()));
    }
}

