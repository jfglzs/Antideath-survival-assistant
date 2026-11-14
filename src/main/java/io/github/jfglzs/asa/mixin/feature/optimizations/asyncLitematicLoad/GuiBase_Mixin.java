package io.github.jfglzs.asa.mixin.feature.optimizations.asyncLitematicLoad;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiBase.class)
public class GuiBase_Mixin {
    @WrapMethod(
            method = "addGuiMessage"
    )
    public void syncAddGuiMessage(Message.MessageType type, int displayTimeMs, String messageKey, Object[] args, Operation<Void> original) {
        if (Configs.Optimizations.ASYNC_LITEMATICA_LOAD.getBooleanValue()) {
            MCUtils.executeTask(() -> original.call(type, displayTimeMs, messageKey, args));
        }
        original.call(type, displayTimeMs, messageKey, args);
    }
}
