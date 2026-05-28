package io.github.jfglzs.asa.mixin.feature.lms;

import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import io.github.jfglzs.asa.utils.lms.ItemStorageDataManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreen_Mixin extends Screen {
    protected AbstractContainerScreen_Mixin(Component title) {
        super(title);
    }

    @Inject(
            method = "onClose",
            at = @At("TAIL")
    )
    private void onClose(CallbackInfo ci) {
        if (Configs.AUTO_KILL_FAKE_PLAYERS.getBooleanValue()) {
            Set<String> fakePlayerNames = ItemStorageDataManager.getFakePlayerNames();
            for (String name : fakePlayerNames) {
                String titleString = this.title.getString();
                if (titleString.contains(name)) {
                    ThreadUtils.runAsync(() -> {
                        try {
                            Thread.sleep(Configs.AUTO_COOLDOWN.getIntegerValue());
                            ThreadUtils.runOnClientThread(() -> MCUtils.executeCommand("player %s kill".formatted(name)));
                            fakePlayerNames.add(name);
                        }
                        catch (InterruptedException e) {
                            ChatUtils.sendMessOnlyClientVisible(e.getMessage());
                            AsaMod.LOGGER.error(e.getMessage(), e);
                        }
                        finally {
                            fakePlayerNames.clear();
                        }
                    });
                    break;
                }
            }
        }
    }
}
