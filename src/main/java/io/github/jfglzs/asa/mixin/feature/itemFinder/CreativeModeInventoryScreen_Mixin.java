package io.github.jfglzs.asa.mixin.feature.itemFinder;
//~ if >= 26.1 'ClickType' -> 'ContainerInput' {
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.render.ItemFinderRenderer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreen_Mixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    public CreativeModeInventoryScreen_Mixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(
            method = "slotClicked",
            at = @At("HEAD")
    )
    private void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput input, CallbackInfo ci) {
        if (Configs.lockCreativeScreen && buttonNum == 2) {
            if (! Configs.DEBUG.getBooleanValue()) return;
            ItemStack stack = this.menu.getCarried();
            if (! stack.isEmpty()) {
                ItemFinderRenderer.stackToFind = stack.copy();
            }
        }
    }
}
//~}
