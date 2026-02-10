package io.jfglzs.asa.mixin.itemscroller;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fi.dy.masa.itemscroller.recipes.RecipePattern;
import io.jfglzs.asa.config.Configs;
import io.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RecipePattern.class)
public class RecipePattern_Mixin {
    @WrapMethod(method = "storeCraftingRecipe")
    public void storeCraftingRecipeWarp(
            Slot slot,
            HandledScreen<? extends ScreenHandler> gui,
            boolean clearIfEmpty,
            boolean fromKeybind,
            MinecraftClient mc,
            Operation<Void> original
    ) {
        if (Configs.TEST.getBooleanValue()) ThreadUtils.threadPool.submit(() -> original.call(slot, gui, clearIfEmpty, fromKeybind, mc));
        else original.call(slot, gui, clearIfEmpty, fromKeybind, mc);
    }
}
