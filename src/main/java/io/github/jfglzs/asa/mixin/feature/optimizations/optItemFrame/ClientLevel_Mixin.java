package io.github.jfglzs.asa.mixin.feature.optimizations.optItemFrame;

import io.github.jfglzs.asa.config.Configs;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ClientLevel.class)
public class ClientLevel_Mixin {
    @Unique private final Int2ObjectMap<MapItemSavedData> ASA$MAPS = new Int2ObjectOpenHashMap<>();

    @Inject(
            method = "getMapData",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getMapData(MapId id, CallbackInfoReturnable<MapItemSavedData> cir) {
        if (Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue()) {
            cir.setReturnValue(this.ASA$MAPS.get(id.id()));
        }
    }

    @Inject(
            method = "overrideMapData",
            at = @At("HEAD"),
            cancellable = true
    )
    public void overrideMapData(MapId id, MapItemSavedData data, CallbackInfo ci) {
        if (Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue()) {
            this.ASA$MAPS.put(id.id(), data);
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
            mapData.forEach((id, data) -> this.ASA$MAPS.put(id.id(), data));
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
            Map<MapId, MapItemSavedData> map = new Object2ObjectArrayMap<>();
            this.ASA$MAPS.forEach((id, data) -> map.put(new MapId(id), data));
            cir.setReturnValue(map);
        }
    }
}
