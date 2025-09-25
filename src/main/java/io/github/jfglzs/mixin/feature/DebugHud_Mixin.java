package io.github.jfglzs.mixin.feature;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.jfglzs.AsaMod.*;
import static io.github.jfglzs.config.Configs.getFeatureAmount;

@Mixin(DebugHud.class)
public class DebugHud_Mixin
{
    @Inject(method = "drawRightText" , at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V"))
    protected void drawRightTextInject(DrawContext context, CallbackInfo ci , @Local List<String> list)
    {
        list.add("");
        list.add(String.format("%s Antideath Survival Assistant V %s" , C_MOD_ID ,version));
        list.add(String.format("%s There are %s Features loaded" , C_MOD_ID , getFeatureAmount()));
    }
}

