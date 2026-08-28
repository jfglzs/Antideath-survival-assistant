package io.github.jfglzs.asa.mixin.feature.optimizations.optSignText;

import io.github.jfglzs.asa.accessor.SignTextAccessor;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignText.class)
public class SignText_Mixin implements SignTextAccessor {
    @Unique private boolean asa$HasText = false;

    @Inject(
            method = "<init>([Lnet/minecraft/network/chat/Component;[Lnet/minecraft/network/chat/Component;Lnet/minecraft/world/item/DyeColor;Z)V",
            at = @At("TAIL")
    )
    private void init(Component[] messages, Component[] filteredMessages, DyeColor color, boolean hasGlowingText,
                      CallbackInfo ci) {
        for (Component message : messages) {
            if (! message.getString().isEmpty()) {
                this.asa$HasText = true;
            }
        }
    }

    @Inject(
            method = "hasMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hasMessage(CallbackInfoReturnable<Boolean> cir) {
        if (Configs.OPT_SIGN_TEXT.getBooleanValue()) {
            cir.setReturnValue(asa$HasText);
        }
    }

    @Override
    public boolean asa$hasText() {
        return asa$HasText;
    }
}
