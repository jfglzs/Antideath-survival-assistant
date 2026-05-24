package io.github.jfglzs.asa.render;

import fi.dy.masa.malilib.interfaces.IRenderer;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static io.github.jfglzs.asa.config.Configs.MATERIAL_TODO_OVERLAY_BOT_FETCH;
import static io.github.jfglzs.asa.config.Configs.MATERIAL_TODO_OVERLAY_GET_ITEM_IMM;

public class MaterialToDoRenderer implements IRenderer {
    public static final MaterialToDoRenderer INSTANCE = new MaterialToDoRenderer();
    public Queue<Item> items = new LinkedList<>();

    private MaterialToDoRenderer() {
    }

    @Override
    public void onRenderGameOverlayPost(GuiGraphics drawContext) {
        if (Configs.ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue()) {
            int xOffset = Configs.MATERIAL_TODO_OVERLAY_X_OFFSET.getIntegerValue();
            int yOffset = Configs.MATERIAL_TODO_OVERLAY_Y_OFFSET.getIntegerValue();
            for (Item item : items) {
                drawContext.renderItem(new ItemStack(item), xOffset, yOffset);
                //~ if <= 1.21.1 '.getName()' -> '.getDescription()' {
                drawContext.drawString(Minecraft.getInstance().font, item.getName(), xOffset + 20, yOffset + 4, 0xFFFFFFFF, true);
                //~}
                yOffset += 18;
            }
        }
    }

    public void update() {
        Queue<Item> newitems = new LinkedList<>();

        for (Item stack : items) {
            if (PlayerUtils.checkRemainCount(stack) > 0) {
                continue;
            }
            newitems.offer(stack);
        }
        items = newitems;
    }

    public void addItem(ItemStack stack) {
        if (items.contains(stack.getItem())) {
            return;
        }

        items.offer(stack.getItem());
        if (MATERIAL_TODO_OVERLAY_GET_ITEM_IMM.getBooleanValue()) {
            this.getItem();
        }
    }

    public void getItem() {
        Item item = items.poll();
        if (item != null) {
            MCUtils.excuteCommand("getItem " + MCUtils.getItemID(item) + " " + MATERIAL_TODO_OVERLAY_BOT_FETCH.getIntegerValue() + " " + "nbt");
        }
    }
}