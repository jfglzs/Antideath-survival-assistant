package io.github.jfglzs.asa.mixin.feature.disableProfiler;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//~ if = 1.21.1 'Profiler' -> 'SingleTickProfiler' {
import net.minecraft.util.profiling.Profiler;

@Mixin(Profiler.class)
//~}
public class Profiler_Mixin {
    //? if > 1.21.1 {
    @Inject(
            method = "get",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGet(CallbackInfoReturnable<ProfilerFiller> cir) {
        if (Configs.DISABLE_PROFILER.getBooleanValue()) {
            cir.setReturnValue(InactiveProfiler.INSTANCE);
        }
    }
    //?}
}
