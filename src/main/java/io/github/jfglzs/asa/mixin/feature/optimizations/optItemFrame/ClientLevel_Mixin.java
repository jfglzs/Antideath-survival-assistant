package io.github.jfglzs.asa.mixin.feature.optimizations.optItemFrame;

import io.github.jfglzs.asa.config.Configs;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ClientLevel.class)
public class ClientLevel_Mixin {
    @Unique private final MapItems ASA$MAPITEMS = new MapItems();

    @Inject(
            method = "getMapData",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getMapData(MapId id, CallbackInfoReturnable<MapItemSavedData> cir) {
        if (Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue()) {
            cir.setReturnValue(this.ASA$MAPITEMS.getMapItem(id));
        }
    }

    @Inject(
            method = "overrideMapData",
            at = @At("HEAD"),
            cancellable = true
    )
    public void overrideMapData(MapId id, MapItemSavedData data, CallbackInfo ci) {
        if (Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue()) {
            this.ASA$MAPITEMS.putMapItem(id, data);
            ci.cancel();
        }
    }

    @Inject(
            method = "addMapData",
            at = @At("HEAD"),
            cancellable = true
    )
    public void addMapData(Map<MapId, MapItemSavedData> mapData, CallbackInfo ci) {
        if (Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue()) {
            this.ASA$MAPITEMS.putAllMaps(mapData);
            ci.cancel();
        }
    }

    @Inject(
            method = "getAllMapData",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getAllMapData(CallbackInfoReturnable<Map<MapId, MapItemSavedData>> cir) {
        if (Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue()) {
            cir.setReturnValue(this.ASA$MAPITEMS.getAllMaps());
        }
    }

    static class MapItems {
        private final Int2ObjectOpenHashMap<MapItemSavedData> MAPS = new Int2ObjectOpenHashMap<>();

        public MapItemSavedData getMapItem(MapId id) {
            return this.MAPS.get(id.id());
        }

        public void putMapItem(MapId id, MapItemSavedData data) {
            this.MAPS.put(id.id(), data);
        }

        public Map<MapId, MapItemSavedData> getAllMaps() {
            Map<MapId, MapItemSavedData> map = new HashMap<>();
            this.MAPS.forEach((id, data) -> map.put(new MapId(id), data));
            return map;
        }

        public void putAllMaps(Map<MapId, MapItemSavedData> map) {
            map.forEach(this::putMapItem);
        }
    }
}
