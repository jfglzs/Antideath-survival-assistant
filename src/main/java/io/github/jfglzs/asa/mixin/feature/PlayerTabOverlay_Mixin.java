package io.github.jfglzs.asa.mixin.feature;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlay_Mixin {
    @ModifyVariable(
            method = "render",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private List<PlayerInfo> renderModify_Variable(List<PlayerInfo> original) {
        if (Configs.TAP_FILTER.getBooleanValue()) {
            var list = new ArrayList<PlayerInfo>();
            for (PlayerInfo entry : original) {
                String name = entry.getProfile().getName();
                if (Configs.ENABLE_TAP_FILTER_WHITELIST.getBooleanValue()) {
                    if (Configs.TAP_FILTER_WHITELIST.getStrings().contains(name)) {
                        list.add(entry);
                    }
                }
                else if (Configs.ENABLE_TAP_FILTER_PREFIX.getBooleanValue()) {
                    for (String string : Configs.TAP_FILTER_PREFIX.getStrings()) {
                        if (name.startsWith(string)) {
                            list.add(entry);
                        }
                    }
                }
                else {
                    if (!Configs.TAP_FILTER_BLACKLIST.getStrings().contains(name)) {
                        list.add(entry);
                    }
                }
            }
            return list;
        }
        else {
            return original;
        }
    }
}

