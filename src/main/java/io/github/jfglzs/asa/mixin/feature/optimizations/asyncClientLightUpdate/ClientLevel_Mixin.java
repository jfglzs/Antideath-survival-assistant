package io.github.jfglzs.asa.mixin.feature.optimizations.asyncClientLightUpdate;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientLevel.class)
public class ClientLevel_Mixin {
    @WrapMethod(
            method = "update"
    )
    public void update(Operation<Void> original) {
        if (Configs.Optimizations.ASYNC_CLIENT_LIGHT_UPDATE.getBooleanValue())
            ThreadUtils.runOnTaskThread(original::call);
        else
            original.call();
    }
}
