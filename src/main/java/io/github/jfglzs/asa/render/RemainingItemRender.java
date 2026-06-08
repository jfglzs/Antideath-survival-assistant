package io.github.jfglzs.asa.render;

import fi.dy.masa.malilib.interfaces.IRenderer;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
//? < 1.21.11
//import net.minecraft.client.gui.GuiGraphics;
//? >= 1.21.11
import fi.dy.masa.malilib.render.GuiContext;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RemainingItemRender implements IRenderer {
    public static final RemainingItemRender INSTANCE = new RemainingItemRender();
    private ItemStack stack;

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

    public void update(Minecraft mc) {
        this.stack = PlayerUtils.getPlayerMainHandStack();
    }

    public void render(GuiContext ctx) {
        if (Configs.DISPLAY_REMAIN_ITEM.getBooleanValue()) {
            int xOffset = Configs.DISPLAY_REMAIN_ITEM_OVERLAY_X_OFFSET.getIntegerValue();
            int yOffset = Configs.DISPLAY_REMAIN_ITEM_OVERLAY_Y_OFFSET.getIntegerValue();
            if (stack != null && !stack.getItem().equals(Items.AIR)) {
                ctx.drawString(Minecraft.getInstance().font, "%s %s".formatted(stack.getHoverName().getString(), PlayerUtils.checkRemainCount(stack.getItem())), xOffset + 20, yOffset + 4, 0xFFFFFFFF,true);
                ctx.renderItem(stack, xOffset, yOffset);
            }
        }
    }
    //~}
}
