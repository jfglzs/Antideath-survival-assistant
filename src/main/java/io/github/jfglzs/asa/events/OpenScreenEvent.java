package io.github.jfglzs.asa.events;

import io.github.jfglzs.asa.events.base.Event;
import net.minecraft.client.gui.screens.Screen;

public class OpenScreenEvent extends Event<Screen> {
    public static final OpenScreenEvent INSTANCE = new OpenScreenEvent();

    private OpenScreenEvent() {
    }
}
