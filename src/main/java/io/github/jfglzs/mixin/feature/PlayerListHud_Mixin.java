package io.github.jfglzs.mixin.feature;

import io.github.jfglzs.config.Configs;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHud_Mixin
{
    @ModifyVariable(
            method = "render",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private List<PlayerListEntry> renderModifyVariable(List<PlayerListEntry> original)
    {
        if (Configs.TAP_FILTER.getBooleanValue())
        {
            return original.stream()
                    .filter(entry ->
                    {
                        String name = entry.getProfile().getName();
                        return Configs.TAP_FILTER_WHITELIST.getStrings().stream().noneMatch(name::equals);
                    })
                    .toList();
        }
        return original;
    }
}

