package io.github.jfglzs.asa.mixin.feature;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugHud_Mixin {
    @Inject(method = "drawLeftText" , at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V"))
    protected void drawLeftTextInject(DrawContext context, CallbackInfo ci , @Local List<String> list) {
        list.add(" ");
        list.add(String.format("[ASA] Antideath Survival Assistant V %s", AsaMod.version));
        list.add(String.format("[ASA] Features loaded %S", Configs.getFeatureAmount()));
    }
}

