package io.github.jfglzs.asa.mixin.feature.optimizations.asyncLitematicLoad;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fi.dy.masa.litematica.data.DataManager;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DataManager.class)
public class DataManager_Mixin {
    @WrapMethod(
            method = "load"
    )
    private static void load(Operation<Void> original) {
        if (Configs.Optimizations.ASYNC_LITEMATICA_LOAD.getBooleanValue()) {
            ThreadUtils.runAsync(original::call);
        }
        else {
            original.call();
        }
    }

    @WrapMethod(
            method = "fromJson"
    )
    private void fromJson(JsonObject obj, Operation<Void> original) {
        synchronized (this) {
            original.call(obj);
        }
    }
}
