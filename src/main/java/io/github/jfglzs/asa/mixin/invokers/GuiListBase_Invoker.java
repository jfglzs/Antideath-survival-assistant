package io.github.jfglzs.asa.mixin.invokers;

import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiListBase.class)
public interface GuiListBase_Invoker<TYPE, WIDGET extends WidgetListEntryBase<TYPE>> {
    @Invoker("getListWidget")
    WidgetListBase<TYPE, WIDGET> asa$getListWidget();
}
