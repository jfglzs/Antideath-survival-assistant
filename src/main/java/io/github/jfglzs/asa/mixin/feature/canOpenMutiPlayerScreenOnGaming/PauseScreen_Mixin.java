package io.github.jfglzs.asa.mixin.feature.canOpenMutiPlayerScreenOnGaming;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public class PauseScreen_Mixin {
    @Inject(
            method = "createPauseMenu",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout;arrangeElements()V")
    )
    private void createPauseMenu(CallbackInfo ci, @Local GridLayout.RowHelper row) {
        if (!Configs.CAN_OPEN_MUTI_PLAYER_SCREEN_ON_GAMING.getBooleanValue()) return;
        row.addChild(Button.builder(Component.translatable("asa.multiplayer.screen.title"), (button) -> {
            MCUtils.setScreen(new JoinMultiplayerScreen(((Screen) (Object) this)));

        }).width(204).build(), 2);
    }
}
