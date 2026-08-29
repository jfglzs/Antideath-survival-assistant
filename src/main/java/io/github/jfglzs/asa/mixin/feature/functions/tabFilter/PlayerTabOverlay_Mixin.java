package io.github.jfglzs.asa.mixin.feature.functions.tabFilter;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.PlayerUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlay_Mixin {
    @ModifyVariable(
            //~ if >= 26.1 'render' -> 'extractRenderState' {
            method = "extractRenderState",
            //~}
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private List<PlayerInfo> renderModify_Variable(List<PlayerInfo> original) {
        if (Configs.Functions.TAP_FILTER.getBooleanValue()) {
            ObjectArrayList<PlayerInfo> list = new ObjectArrayList<>();
            for (PlayerInfo entry : original) {
                var name = PlayerUtils.getName(entry.getProfile());
                if (Configs.Functions.ENABLE_TAP_FILTER_WHITELIST.getBooleanValue()) {
                    if (Configs.isInList(name, Configs.Lists.TAP_FILTER_WHITELIST))
                        list.add(entry);
                }
                else if (Configs.Functions.ENABLE_TAP_FILTER_PREFIX.getBooleanValue()) {
                    for (String string : Configs.Lists.TAP_FILTER_PREFIX.getStrings()) {
                        if (! name.startsWith(string))
                            list.add(entry);
                    }
                }
                else {
                    if (! Configs.isInList(name, Configs.Lists.TAP_FILTER_BLACKLIST))
                        list.add(entry);
                }
            }
            return list;
        }
        return original;
    }
}

