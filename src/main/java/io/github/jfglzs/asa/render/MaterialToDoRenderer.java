package io.github.jfglzs.asa.render;

import fi.dy.masa.malilib.interfaces.IRenderer;
//? >= 1.21.11
import fi.dy.masa.malilib.render.GuiContext;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
//? < 1.21.11
//import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class MaterialToDoRenderer implements IRenderer {
    public static final MaterialToDoRenderer INSTANCE = new MaterialToDoRenderer();
    public Queue<Item> items = new LinkedList<>();

    private MaterialToDoRenderer() {
    }

    //~ if < 1.21.11 'GuiContext' -> 'GuiGraphics' {
        //? if < 26.1 {
        /*@Override
        public void onRenderGameOverlayPost(GuiContext ctx) {
            this.render(ctx);
        }
        *///?} else {
    @Override
    public void onExtractGuiOverlayPost(GuiContext ctx, float partialTicks, ProfilerFiller profiler) {
        this.render(ctx);
    }
        //?}

    public void render(GuiContext ctx) {
        if (Configs.ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue()) {
            int xOffset = Configs.MATERIAL_TODO_OVERLAY_X_OFFSET.getIntegerValue();
            int yOffset = Configs.MATERIAL_TODO_OVERLAY_Y_OFFSET.getIntegerValue();
            for (Item item : items) {
                ctx.renderItem(new ItemStack(item), xOffset, yOffset);
                //? if <= 1.21.1 {
                 /*ctx.drawString(Minecraft.getInstance().font, item.getDescription(), xOffset + 20, yOffset + 4, 0xFFFFFFFF, true);
                *///?} else if >=26.1 {
                ctx.drawString(Minecraft.getInstance().font, item.getDescriptionId(), xOffset + 20, yOffset + 4, 0xFFFFFFFF, true);
                //?} else {
                 /*ctx.drawString(Minecraft.getInstance().font, item.getName(), xOffset + 20, yOffset + 4, 0xFFFFFFFF, true);
                *///?}
                yOffset += 18;
            }
        }
    }
    //~}

    public void update(Minecraft mc) {
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
        if (items.contains(stack.getItem()) || Configs.ENABLE_MATERIAL_TODO_OVERLAY.getBooleanValue()) {
            return;
        }

        items.offer(stack.getItem());
    }
}