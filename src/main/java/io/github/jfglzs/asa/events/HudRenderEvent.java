package io.github.jfglzs.asa.events;

import fi.dy.masa.malilib.interfaces.IRenderer;
import io.github.jfglzs.asa.events.base.Event;
//? if < 1.21.11 {
//import net.minecraft.client.gui.GuiGraphics;
//?} else {
import fi.dy.masa.malilib.render.GuiContext;
import net.minecraft.util.profiling.ProfilerFiller;
//?}

public class HudRenderEvent extends Event<HudRenderEvent.RenderContextWrap> implements IRenderer {
    public static final HudRenderEvent INSTANCE = new HudRenderEvent();

    //~ if < 1.21.11 'GuiContext' -> 'GuiGraphics' {
    //? if < 26.1 {
    /*@Override
    public void onRenderGameOverlayPost(GuiContext ctx) {
        this.update(new RenderContextWrap(ctx));
    }
    *///?} else {
    @Override
    public void onExtractGuiOverlayPost(GuiContext ctx, float partialTicks, ProfilerFiller profiler) {
        this.update(new RenderContextWrap(ctx));
    }
    //?}

    public record RenderContextWrap(GuiContext context) {
    }
}
//~}


