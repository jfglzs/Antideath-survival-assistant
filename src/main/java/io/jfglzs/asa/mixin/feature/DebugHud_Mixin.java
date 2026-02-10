package io.jfglzs.asa.mixin.feature;

import io.jfglzs.asa.AsaMod;
import io.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugHud_Mixin {
    @Unique private String s1 = String.format("\n%s Antideath Survival Assistant V %s" , AsaMod.C_MOD_ID , AsaMod.version);
    @Unique private String s2 = String.format("%s %s Features loaded" , AsaMod.C_MOD_ID , Configs.getFeatureAmount());

    @ModifyArg(
            method = "drawLeftText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V"
            ),
            index = 1
    )
    private List<String> drawTextModifyArgs(List<String> text) {
        text.add(s1);
        text.add(s2);
        return text;
    }
}

