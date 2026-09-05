package io.github.jfglzs.asa.mixin.feature.functions.spectatorTeleport;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.jfglzs.asa.feature.spectatorTeleport.FakePlayerUtils;
import io.github.jfglzs.asa.feature.spectatorTeleport.TeleportToFakePlayerMenuCategory;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayerMenuCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(TeleportToPlayerMenuCategory.class)
public abstract class TeleportToPlayerMenuCategory_Mixin {
    // 都怪 TweakerMore 讓它那麼複雜。
    @ModifyReturnValue(method = "getItems", at = @At("RETURN"))
    private List<SpectatorMenuItem> filterFakePlayers(List<SpectatorMenuItem> items) {
        return filter(items);
    }

    @ModifyReceiver(
            method = "isEnabled",
            at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z")
    )
    private List<SpectatorMenuItem> filterFakePlayersForEnabledCheck(List<SpectatorMenuItem> items) {
        return filter(items);
    }

    @Unique
    private List<SpectatorMenuItem> filter(List<SpectatorMenuItem> items) {
        if (items == null) return List.of();
        return (Object) this instanceof TeleportToFakePlayerMenuCategory
                ? items
                : FakePlayerUtils.withoutFakePlayers(items);
    }
}
