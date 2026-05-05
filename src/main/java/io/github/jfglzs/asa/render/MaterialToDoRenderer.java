package io.github.jfglzs.asa.render;

import fi.dy.masa.malilib.interfaces.IRenderer;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.itemdisplay.RemainingItemDisplayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class MaterialToDoRenderer implements IRenderer {
    public static final MaterialToDoRenderer INSTANCE = new MaterialToDoRenderer();
    public Set<ItemStack> itemStacks = new HashSet<>();

    private MaterialToDoRenderer() {
    }

    @Override
    public void onRenderGameOverlayPost(DrawContext drawContext) {
        if (Configs.ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue()) {
            int xOffset = Configs.MATERIAL_TODO_OVERLAY_X_OFFSET.getIntegerValue();
            int yOffset = Configs.MATERIAL_TODO_OVERLAY_Y_OFFSET.getIntegerValue();
            for (ItemStack stack : itemStacks) {
                drawContext.drawItem(stack, xOffset, yOffset);
                drawContext.drawText(MinecraftClient.getInstance().textRenderer, stack.getName(), xOffset + 20, yOffset + 4, 0xFFFFFFFF,true);
                yOffset += 18;
            }
        }
    }

    public void update() {
        Set<ItemStack> newitemStacks = new HashSet<>();
        for (ItemStack stack : itemStacks) {
            if (RemainingItemDisplayer.checkRemainCount(stack.getItem()) > 0) {
                continue;
            }
            newitemStacks.add(stack);
        }
        itemStacks = newitemStacks;
    }

    public void addItem(ItemStack stack) {
        itemStacks.add(stack);
    }
}
