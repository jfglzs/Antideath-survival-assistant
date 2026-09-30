package io.github.jfglzs.asa.accessor;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;

public interface IClientPacketListenerAccessor1 {
    Int2IntArrayMap asa$getMaps();

    static IClientPacketListenerAccessor1 of(Object object) {
        return (IClientPacketListenerAccessor1) object;
    }
}
