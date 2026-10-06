package io.github.jfglzs.asa.mixin.feature.optimizations.asyncLitematicLoad;

//? if > 1.21.1 {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.ProgressBar;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(LitematicaSchematic.class)
public abstract class LitematicaSchematic_Mixin {
    @Unique private RegionInfo asa$curRegion = null;

    @WrapMethod(
            method = "convertTileEntities_to_1_20_5"
    )
    private Map<?, ?> convertTileEntities_to_1_20_5(Map<?, ?> oldTE, int minecraftDataVersion, Operation<Map<?, ?>> original) {
        if (! Configs.Optimizations.ASYNC_LITEMATICA_LOAD.getBooleanValue())
            return original.call(oldTE, minecraftDataVersion);

        var list = new ArrayList<>(oldTE.entrySet());
        int threads = Configs.Optimizations.ASYNC_LITEMATICA_LOAD_THREAD_AMOUNT.getIntegerValue();
        var results = new ConcurrentHashMap<>();

        ThreadUtils.parallel(list, threads, 500, entries -> {
            var result = new HashMap<>();
            entries.forEach(entry -> result.put(entry.getKey(), entry.getValue()));
            results.putAll(original.call(result, minecraftDataVersion));
        });

        return results;
    }

    @WrapOperation(
            method = "convertTileEntities_to_1_20_5",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"
            )
    )
    void info(Logger instance, String s, Object ob1, Object ob2, Operation<?> original) {
        if (! Configs.Optimizations.ASYNC_LITEMATICA_LOAD.getBooleanValue())
            instance.info(s, ob1, ob2);
    }

    @Inject(
            method = "convertTileEntities_to_1_20_5",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private void convertTileEntities_to_1_20_5(CallbackInfoReturnable<?> cir) {
        asa$updateProgress("ConvertTileEntities");
    }

    @SuppressWarnings("all")
    @ModifyVariable(
            method = {"readSubRegionsFromData", "readSubRegionsFromNBT"},
            at = @At("STORE"),
            name = "tiles",
            require = 1
    )
    private Map<?, ?> modifyTiles(Map<?, ?> tiles, @Local(name = "regionName") String regionName) {
        if (tiles != null) {
            asa$curRegion = new RegionInfo(regionName, new AtomicInteger(tiles.size()), new AtomicInteger(0));
        }

        return tiles;
    }

    @Unique
    private void asa$updateProgress(String name) {
        double progressValue = (double) asa$curRegion.cur().incrementAndGet() / asa$curRegion.total().get();
        String progressText = asa$curRegion.regionName() + "[" + name + "]";
        Component progress = ProgressBar.getProgress(progressValue, progressText);
        ThreadUtils.runOnClientThread(() -> ChatUtils.actionBar(progress));
    }

    record RegionInfo(String regionName, AtomicInteger total, AtomicInteger cur) {
    }
}
//?} else {
//@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.asa.utils.DummyClass.class)
//public abstract class LitematicaSchematic_Mixin {
//
//}
//?}

