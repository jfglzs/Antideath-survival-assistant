package io.github.jfglzs.asa.mixin.feature;

import net.minecraft.client.network.ClientCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Collections;

@Mixin(ClientCommandSource.class)
public class ClientCommandSource_Mixin {
    @Inject(
            method = "getChatSuggestions",
            at = @At("RETURN"),
            cancellable = true
    )
    public void getChatSuggestions_Inject(CallbackInfoReturnable<Collection<String>> cir) {
        cir.setReturnValue(Collections.singleton("!!pb make"));
    }
}
