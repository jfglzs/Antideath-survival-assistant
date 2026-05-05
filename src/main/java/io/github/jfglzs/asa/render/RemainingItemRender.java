package io.github.jfglzs.asa.render;

import fi.dy.masa.malilib.interfaces.IRenderer;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class RemainingItemRender implements IRenderer {
    public static final RemainingItemRender INSTANCE = new RemainingItemRender();
    public ItemStack stack;

    @Override
    public void onRenderGameOverlayPost(DrawContext drawContext) {
        if (Configs.DISPLAY_REMAIN_ITEM.getBooleanValue()) {
            int xOffset = Configs.DISPLAY_REMAIN_ITEM_OVERLAY_X_OFFSET.getIntegerValue();
            int yOffset = Configs.DISPLAY_REMAIN_ITEM_OVERLAY_Y_OFFSET.getIntegerValue();
            if (stack != null && !stack.getItem().equals(Items.AIR)) {
                drawContext.drawText(MinecraftClient.getInstance().textRenderer, "%s %s".formatted(stack.getName().getString(), PlayerUtils.checkRemainCount(stack.getItem())), xOffset + 20, yOffset + 4, 0xFFFFFFFF,true);
                drawContext.drawItem(stack, xOffset, yOffset);
            }
        }
    }
}
