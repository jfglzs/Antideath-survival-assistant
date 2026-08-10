package io.github.jfglzs.asa.mixin.feature.optSignText;

import io.github.jfglzs.asa.accessor.SignTextAccessor;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "betterblockentities.client.render.immediate.blockentity.manager.SpecialBlockEntityManager")
public class BBE_SpecialBlockEntityManager_Mixin {
    @Inject(
            method = "hasAnyText",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void hasAnyText(SignText text, boolean filtered, CallbackInfoReturnable<Boolean> cir) {
        if (Configs.OPT_SIGN_TEXT.getBooleanValue()) {
            cir.setReturnValue(((SignTextAccessor) text).asa$hasText());
        }
    }
}
