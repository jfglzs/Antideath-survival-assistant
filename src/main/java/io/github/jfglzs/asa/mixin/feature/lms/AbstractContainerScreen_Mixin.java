package io.github.jfglzs.asa.mixin.feature.lms;

import com.google.common.collect.ImmutableList;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreen_Mixin<T extends AbstractContainerMenu> extends Screen {
    @Shadow
    @Final
    protected T menu;

    protected AbstractContainerScreen_Mixin(Component title) {
        super(title);
    }

    @Inject(
            method = "onClose",
            at = @At("TAIL")
    )
    private void onClose(CallbackInfo ci) {
        asa$cacheData();
        if (Configs.AUTO_KILL_FAKE_PLAYERS.getBooleanValue()) {
            Set<String> fakePlayerNames = ItemStorageDataManager.getFakePlayerNames();
            for (String name : fakePlayerNames) {
                var titleString = this.title.getString();
                if (titleString.contains(name)) {

                    ThreadUtils.runOnTaskThread(() -> {
                        try {
                            Thread.sleep(Configs.AUTO_COOLDOWN.getIntegerValue());
                            ThreadUtils.runOnClientThread(() -> MCUtils.executeCommand("player %s kill".formatted(name)));
                        }
                        catch (Exception e) {
                            ChatUtils.sendMessOnlyClientVisible(ChatUtils.c(e.getMessage()));
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

    @Unique
    private void asa$cacheData() {
        if (Configs.FAKE_PLAYER_INVENTORY_ITEM_CACHE.getBooleanValue()) {
            String title = this.title.getString();
            List<String> names = Configs.FAKE_PLAYER_INVENTORY_ITEM_CACHE_WHITE_LIST.getStrings();
            for (String name : names) {
                if (title.contains(name)) {
                    ItemStorageDataManager.addPlayerInventory(name, new ItemStorageDataManager.PlayerInventory(ImmutableList.copyOf(this.menu.slots)));
                    ChatUtils.sendOverLayMessage(ChatUtils.c("已缓存 %s 的物品栏".formatted(name)));
                    break;
                }
            }
        }
    }
}
