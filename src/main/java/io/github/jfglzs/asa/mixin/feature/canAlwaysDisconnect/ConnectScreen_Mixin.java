package io.github.jfglzs.asa.mixin.feature.canAlwaysDisconnect;

import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ConnectScreen.class)
public abstract class ConnectScreen_Mixin extends Screen {
    protected ConnectScreen_Mixin(Component title) {
        super(title);
    }


}

