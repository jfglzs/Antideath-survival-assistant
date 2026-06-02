package io.github.jfglzs.asa.mixin.feature.fpsOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Comparator;
import java.util.List;

@Mixin(LevelRenderer.class)
public class LevelRenderer_Mixin {
    //? if = 1.21.8 {
    @WrapOperation(
            method = "method_62214",
            at = @At(value = "INVOKE", target = "Ljava/util/List;sort(Ljava/util/Comparator;)V")
    )
    private static void sort(List instance, Comparator<Object> c, Operation<Void> original) {
        if (!Configs.FPS_OPTIMIZATION.getBooleanValue()) {
            original.call(instance, c);
        }
    }
    //?}
}
