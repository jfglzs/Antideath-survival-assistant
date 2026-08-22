package io.github.jfglzs.asa.render;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.events.HudRenderEvent;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RemainingItemRender {
    public static ItemStack stack;

    public static void init() {
        HudRenderEvent.INSTANCE.register(RemainingItemRender::render);
    }

    public static void tick(Minecraft mc) {
        if (Configs.DISPLAY_REMAIN_ITEM.getBooleanValue()) {
            stack = PlayerUtils.getPlayerMainHandStack();
        }
    }

    public static void render(HudRenderEvent.RenderContextWrap wrap) {
        if (Configs.DISPLAY_REMAIN_ITEM.getBooleanValue()) {
            int xOffset = Configs.DISPLAY_REMAIN_ITEM_OVERLAY_X_OFFSET.getIntegerValue();
            int yOffset = Configs.DISPLAY_REMAIN_ITEM_OVERLAY_Y_OFFSET.getIntegerValue();
            if (stack != null && ! stack.is(Items.AIR)) {
                var ctx = wrap.context();
                ctx.drawString(Minecraft.getInstance().font, "%s %s".formatted(stack.getHoverName().getString(), PlayerUtils.checkRemainCount(stack.getItem())), xOffset + 20, yOffset + 4, 0xFFFFFFFF, true);
                ctx.renderItem(stack, xOffset, yOffset);
            }
        }
    }
}
