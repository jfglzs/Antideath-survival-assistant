package io.github.jfglzs.asa.mixin.feature.functions.fakePlayerMenu;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.spectatorTeleport.TeleportToFakePlayerMenuCategory;
import net.minecraft.client.gui.spectator.RootSpectatorMenuCategory;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RootSpectatorMenuCategory.class)
public abstract class RootSpectatorMenuCategory_Mixin {
    @Shadow
    @Final
    private List<SpectatorMenuItem> items;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void addFakePlayerCategory(CallbackInfo ci) {
        if (Configs.Functions.FAKE_PLAYER_TELEPORT_MENU.getBooleanValue())
            items.add(new TeleportToFakePlayerMenuCategory());
    }
}
