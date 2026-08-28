package io.github.jfglzs.asa.render;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.events.HudRenderEvent;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class MaterialToDoRenderer {
    public static Queue<Item> items = new LinkedList<>();

    public static void init() {
        HudRenderEvent.INSTANCE.register(MaterialToDoRenderer::render);
    }

    public static void render(HudRenderEvent.RenderContextWrap wrap) {
        if (Configs.ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue()) {
            int xOffset = Configs.MATERIAL_TODO_OVERLAY_X_OFFSET.getIntegerValue();
            int yOffset = Configs.MATERIAL_TODO_OVERLAY_Y_OFFSET.getIntegerValue();
            for (Item item : items) {
                var ctx = wrap.context();
                ctx.renderItem(new ItemStack(item), xOffset, yOffset);
                Font font = Minecraft.getInstance().font;
                //? if <= 1.21.1 {
                /*ctx.drawString(font, item.getDescription(), xOffset + 20, yOffset + 4, 0xFFFFFFFF, true);
                 *///?} else if >= 26.1 {
                ctx.drawString(font, item.getDescriptionId(), xOffset + 20, yOffset + 4, 0xFFFFFFFF, true);
                //?} else {
                /*ctx.drawString(font, item.getName(), xOffset + 20, yOffset + 4, 0xFFFFFFFF, true);
                 *///?}
                yOffset += 18;
            }
        }
    }

    public static void tick(Minecraft mc) {
        Queue<Item> newItems = new LinkedList<>();

        for (Item stack : items) {
            if (PlayerUtils.checkRemainCount(stack) > 0) {
                continue;
            }
            newItems.offer(stack);
        }
        items = newItems;
    }

    public static void addItem(ItemStack stack) {
        if (items.contains(stack.getItem()) || Configs.ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue()) {
            return;
        }

        items.offer(stack.getItem());
    }
}