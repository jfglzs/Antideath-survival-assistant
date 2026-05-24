package io.github.jfglzs.asa.mixin.feature.lms;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.lms.ItemStorageDataManager;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreen_Mixin extends HandledScreen<CreativeInventoryScreen.CreativeScreenHandler> {
    @Unique
    private SlotActionType type;

    public CreativeInventoryScreen_Mixin(CreativeInventoryScreen.CreativeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(
            method = "onMouseClick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/screen/slot/SlotActionType;THROW:Lnet/minecraft/screen/slot/SlotActionType;",
                    ordinal = 0,
                    opcode = Opcodes.GETSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    protected void onMouseClick_1(
            Slot slot,
            int slotId,
            int button,
            SlotActionType actionType,
            CallbackInfo ci
    ) {
        if (Configs.lockCreativeScreen && actionType != SlotActionType.THROW && actionType != SlotActionType.QUICK_CRAFT) {
            type = actionType;
        }
    }

    @Inject(
            method = "handledScreenTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isInCreativeMode()Z"),
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
            method = "onMouseClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;canDropItems()Z", ordinal = 1),
            cancellable = true
    )
    protected void onMouseClick_2(
            Slot slot,
            int slotId,
            int button,
            SlotActionType actionType,
            CallbackInfo ci
    ) {
        if (Configs.lockCreativeScreen && type != null && slotId == -999) {
            ItemStack stack = this.handler.getCursorStack();
            int count = -1;
            if (type == SlotActionType.PICKUP && button == 0) count = stack.getMaxCount();
            else if (type == SlotActionType.PICKUP && button == 1) count = 1728;
            ItemStorageDataManager.submit(stack.getItem(), stack.getCount() * count);
            handler.setCursorStack(ItemStack.EMPTY);
            Configs.lockCreativeScreen = false;
            this.client.setScreen(null);
            this.type = null;
            ci.cancel();
        }
    }

    @Inject(
            method = "getTooltipFromItem",
            at = @At("RETURN"),
            cancellable = true
    )
    public void getTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<Text>> cir) {
        if (Configs.LMS_FETCH_SUPPORT.getBooleanValue() && Configs.lockCreativeScreen) {
            if (client.player != null && !client.player.isCreative() && Configs.LMS_FETCH_SUPPORT.getBooleanValue()) {
                var texts = cir.getReturnValue();
                var text = ItemStorageDataManager.get(stack);
                texts.add(text);
                cir.setReturnValue(texts);
            }
        }
    }

    @ModifyExpressionValue(
            method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isInCreativeMode()Z")
    )
    public boolean isInCreativeMode(boolean original) {
        return Configs.lockCreativeScreen || original;
    }

    @ModifyExpressionValue(
            method = "shouldShowOperatorTab",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isCreativeLevelTwoOp()Z")
    )
    public boolean isCreativeLevelTwoOp(boolean original) {
        if (Configs.LMS_FETCH_SUPPORT.getBooleanValue() && Configs.lockCreativeScreen) {
            return true;
        }
        return original;
    }
}