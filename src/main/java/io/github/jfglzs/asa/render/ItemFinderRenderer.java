package io.github.jfglzs.asa.render;

import fi.dy.masa.litematica.materials.MaterialListHudRenderer;
import fi.dy.masa.malilib.util.data.Color4f;
import io.github.jfglzs.asa.events.HudRenderEvent;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;

public class ItemFinderRenderer {
    private static final Color4f COLOR_4_F = new Color4f(255f, 48f, 255f);
    public static ItemStack stackToFind = ItemStack.EMPTY;

    public static void init() {
        HudRenderEvent.INSTANCE.register(ItemFinderRenderer::render);
    }

    private static void render(HudRenderEvent.RenderContextWrap wrap) {
        if (stackToFind.isEmpty()) return;
        if (MCUtils.getScreen() instanceof AbstractContainerScreen<?> gui) {
            //? if > 1.21.5 {
            var ctx = wrap.context();
            MaterialListHudRenderer.highlightSlotsWithItem(ctx, stackToFind, gui, COLOR_4_F, MCUtils.getMinecraft());
            //?} else {
            //MaterialListHudRenderer.highlightSlotsWithItem(stackToFind, gui, COLOR_4_F, MCUtils.getMinecraft())
            //?}
        }
    }
}
