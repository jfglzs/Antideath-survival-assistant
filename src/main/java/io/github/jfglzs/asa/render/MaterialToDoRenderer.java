package io.github.jfglzs.asa.render;

import fi.dy.masa.malilib.interfaces.IRenderer;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class MaterialToDoRenderer implements IRenderer {
    public static final MaterialToDoRenderer INSTANCE = new MaterialToDoRenderer();
    public Set<Item> items = new HashSet<>();

    private MaterialToDoRenderer() {
    }

    @Override
    public void onRenderGameOverlayPost(DrawContext drawContext) {
        if (Configs.ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue()) {
            int xOffset = Configs.MATERIAL_TODO_OVERLAY_X_OFFSET.getIntegerValue();
            int yOffset = Configs.MATERIAL_TODO_OVERLAY_Y_OFFSET.getIntegerValue();
            for (Item item : items) {
                drawContext.drawItem(new ItemStack(item), xOffset, yOffset);
                drawContext.drawText(MinecraftClient.getInstance().textRenderer, item.getName(), xOffset + 20, yOffset + 4, 0xFFFFFFFF,true);
                yOffset += 18;
            }
        }
    }

    public void update() {
        Set<Item> newitemStacks = new HashSet<>();
        for (Item stack : items) {
            if (PlayerUtils.checkRemainCount(stack) > 0) {
                continue;
            }
            newitemStacks.add(stack);
        }
        items = newitemStacks;
    }

    public void addItem(ItemStack stack) {
        items.add(stack.getItem());
    }
}
