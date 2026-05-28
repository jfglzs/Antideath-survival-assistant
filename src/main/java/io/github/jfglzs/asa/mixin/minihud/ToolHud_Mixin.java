package io.github.jfglzs.asa.mixin.minihud;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.datafixers.kinds.IdF;
import fi.dy.masa.litematica.render.infohud.ToolHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ToolHud.class)
public class ToolHud_Mixin {
    @ModifyReceiver(
            method = "updateHudText",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    )
    protected List<String> updateHudText(List<String> instance, Object e) {

    }
}
