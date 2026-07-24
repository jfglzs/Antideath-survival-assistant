package io.github.jfglzs.asa.mixin.feature.lms;
//~ if >= 26.1 'ClickType' -> 'ContainerInput' {
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.lms.ItemStorageDataManager;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.network.chat.Component;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

//~ if <=1.21.4 'player/LocalPlayer;hasInfiniteMaterials' -> 'multiplayer/MultiPlayerGameMode;hasInfiniteItems' {
@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreen_Mixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    public CreativeModeInventoryScreen_Mixin(CreativeModeInventoryScreen.ItemPickerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Inject(
            method = "slotClicked",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/inventory/ContainerInput;THROW:Lnet/minecraft/world/inventory/ContainerInput;",
                    ordinal = 0,
                    opcode = Opcodes.GETSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    protected void onMouseClick_1(
            Slot slot,
            int slotId,
            int button,
            ContainerInput actionType,
            CallbackInfo ci,
            @Share("type") LocalRef<ContainerInput> type
    ) {
        if (
                Configs.lockCreativeScreen
                && actionType != ContainerInput.THROW
                && actionType != ContainerInput.QUICK_CRAFT
        ) {
            type.set(actionType);
        }
    }

    @Inject(
            method = "containerTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasInfiniteMaterials()Z"),
            cancellable = true
    )
    public void handledScreenTick(CallbackInfo ci) {
        if (Configs.lockCreativeScreen) {
            ci.cancel();
        }
    }

    /*
        slotID -999 (从创造模式物品栏丢出物品)
        button 0 左键
        button 1 右键
        SlotActionType QuickMove（shift）
        SlotActionType PickUp 单击
    */
    @Inject(
            method = "slotClicked",
            //~ if <=1.21.4 'player/LocalPlayer;canDropItems()Z' -> 'multiplayer/MultiPlayerGameMode;handleCreativeModeItemDrop(Lnet/minecraft/world/item/ItemStack;)V' {
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;canDropItems()Z", ordinal = 1),
            //~}
            cancellable = true
    )
    protected void onMouseClick_2(
            Slot slot,
            int slotId,
            int button,
            ContainerInput actionType,
            CallbackInfo ci,
            @Share("type") LocalRef<ContainerInput> type
    ) {
        if (Configs.lockCreativeScreen && type.get() != null && slotId == -999) {
            ItemStack stack = this.menu.getCarried();
            int count = -1;
            int maxCount = stack.getMaxStackSize();
            if (type.get() == ContainerInput.PICKUP && button == 0) count = maxCount;
            else if (type.get() == ContainerInput.PICKUP && button == 1) count = maxCount * 27;
            ItemStorageDataManager.submit(stack.getItem(), stack.getCount() * count);
            menu.setCarried(ItemStack.EMPTY);
            Configs.lockCreativeScreen = false;
            MCUtils.setScreen(null);
            ci.cancel();
        }
    }

    @Inject(
            method = "getTooltipFromContainerItem",
            at = @At("RETURN"),
            cancellable = true
    )
    public void getTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<Component>> cir) {
        if (Configs.LMS_FETCH_SUPPORT.getBooleanValue() && Configs.lockCreativeScreen) {
            if (minecraft.player != null && !minecraft.player.isCreative() && Configs.LMS_FETCH_SUPPORT.getBooleanValue()) {
                var texts = cir.getReturnValue();
                var text = ItemStorageDataManager.get(stack);
                texts.addAll(text);
                cir.setReturnValue(texts);
            }
        }
    }

    @ModifyExpressionValue(
            method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasInfiniteMaterials()Z")
    )
    public boolean isInCreativeMode(boolean original) {
        return Configs.lockCreativeScreen || original;
    }

    @ModifyExpressionValue(
            method = "hasPermissions",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;canUseGameMasterBlocks()Z")
    )
    public boolean isCreativeLevelTwoOp(boolean original) {
        if (Configs.LMS_FETCH_SUPPORT.getBooleanValue() && Configs.lockCreativeScreen) {
            return true;
        }
        return original;
    }
}
//~}
//~}