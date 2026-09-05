package io.github.jfglzs.asa.mixin.feature.functions.spectatorTeleport;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.spectatorTeleport.TeleportToFakePlayerMenuCategory;
import net.minecraft.client.gui.spectator.RootSpectatorMenuCategory;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(RootSpectatorMenuCategory.class)
public abstract class RootSpectatorMenuCategory_Mixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 1
            )
    )
    private boolean addFakePlayerCategory(List<SpectatorMenuItem> items, Object teamCategory, Operation<Boolean> original) {
        boolean added = original.call(items, teamCategory);
        if (Configs.Functions.SPECTATOR_TELEPORT_FAKE_PLAYER_LIST.getBooleanValue()) {
            items.add(new TeleportToFakePlayerMenuCategory());
        }
        return added;
    }
}
