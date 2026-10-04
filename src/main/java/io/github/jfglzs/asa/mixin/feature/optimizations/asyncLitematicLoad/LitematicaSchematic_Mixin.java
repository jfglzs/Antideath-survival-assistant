package io.github.jfglzs.asa.mixin.feature.optimizations.asyncLitematicLoad;
//? if > 1.21.1 {
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionMaps;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.ProgressBar;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(LitematicaSchematic.class)
public abstract class LitematicaSchematic_Mixin {
    @Shadow
    protected abstract Map<BlockPos, CompoundTag> readTileEntitiesFromNBT(ListTag tagList);

    @Shadow
    protected abstract Map<BlockPos, CompoundTag> readTileEntitiesFromNBT_v1(ListTag tagList);

    @Unique private RegionInfo asa$curRegion = null;

    @Inject(
            method = "convertTileEntities_to_1_20_5",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/HashMap;<init>()V"
            ),
            cancellable = true
    )
    private void convertTileEntities_to_1_20_5(Map<BlockPos, CompoundTag> oldTE, int minecraftDataVersion, CallbackInfoReturnable<Map<BlockPos, CompoundTag>> cir) {
        if (! Configs.Optimizations.ASYNC_LITEMATICA_LOAD.getBooleanValue())
            return;

        Map<BlockPos, CompoundTag> map = new ConcurrentHashMap<>();
        int threads = Configs.Optimizations.ASYNC_LITEMATICA_LOAD_THREAD_AMOUNT.getIntegerValue();
        var list = new ArrayList<>(oldTE.entrySet());
        ThreadUtils.parallel(list, threads, list.size() / threads, entries -> {
            for (Map.Entry<BlockPos, CompoundTag> entry : entries) {
                map.put(entry.getKey(), SchematicConversionMaps.updateBlockEntity(SchematicConversionMaps.checkForIdTag(entry.getValue()), minecraftDataVersion));
                asa$updateProgress("ConvertTileEntities");
            }
        });

        cir.setReturnValue(map);
    }

    @Inject(
            method = "convertEntities_to_1_20_5",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/ListTag;add(Ljava/lang/Object;)Z"
            )
    )
    private void convertEntities_to_1_20_5(CallbackInfoReturnable<ListTag> cir) {
        asa$updateProgress("ConvertingEntities");
    }

    @Inject(
            method = "readSubRegionsFromNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 0
            )
    )
    private void readSubRegionsFromNBT(CompoundTag tag, int version, int minecraftDataVersion, CallbackInfo ci,
                                       @Local(ordinal = 0) BlockPos regionPos, @Local(ordinal = 1) BlockPos regionSize,
                                       @Local(ordinal = 1) CompoundTag regionTag, @Local String regionName) {
        //~ if >= 1.21.5 'getList(' -> 'getListOrEmpty(' {
        //~ if >= 1.21.5 '",fi.dy.masa.malilib.util.data.Constants.NBT.TAG_COMPOUND)' -> '")' {
        if (regionPos != null && regionSize != null) {
            int total = 0;

            if (version >= 2) {
                total += this.readTileEntitiesFromNBT(regionTag.getListOrEmpty("TileEntities")).size();
                total += regionTag.getListOrEmpty("Entities").size();
            }
            else if (version == 1) {
                total += this.readTileEntitiesFromNBT_v1(regionTag.getListOrEmpty("TileEntities")).size();
            }
            if (version >= 3) {
                total += regionTag.getListOrEmpty("PendingBlockTicks").size();
            }
            if (version >= 5) {
                total += regionTag.getListOrEmpty("PendingFluidTicks").size();
            }

            asa$curRegion = new RegionInfo(regionName, new AtomicInteger(total), new AtomicInteger(0));
        }
        //~}
        //~}
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

