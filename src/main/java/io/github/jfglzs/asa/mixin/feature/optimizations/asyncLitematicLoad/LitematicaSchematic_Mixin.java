package io.github.jfglzs.asa.mixin.feature.optimizations.asyncLitematicLoad;
//? if >1.21.1 {

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionMaps;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.ProgressBar;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.core.BlockPos;
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

//~ if >=26.3 'net.minecraft.nbt.CompoundTag' -> 'fi.dy.masa.malilib.util.data.tag.CompoundData' {
//~ if >= 26.3 'net.minecraft.nbt.ListTag' -> 'fi.dy.masa.malilib.util.data.tag.ListData' {
@Mixin(LitematicaSchematic.class)
public abstract class LitematicaSchematic_Mixin {
    //~ if >= 26.3 'readTileEntitiesFromNBT' -> 'readTileEntitiesFromData' {
    @Shadow
    protected abstract Map<BlockPos, net.minecraft.nbt.CompoundTag> readTileEntitiesFromNBT(net.minecraft.nbt.ListTag par1);

    @Shadow
    protected abstract Map<BlockPos, net.minecraft.nbt.CompoundTag> readTileEntitiesFromNBT_v1(net.minecraft.nbt.ListTag par1);
    //~}

    @Unique private RegionInfo asa$curRegion = null;

    @Inject(
            method = "convertTileEntities_to_1_20_5",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/HashMap;<init>()V"
            ),
            cancellable = true
    )
    private void convertTileEntities_to_1_20_5(Map<BlockPos, net.minecraft.nbt.CompoundTag> oldTE,
                                               int minecraftDataVersion, CallbackInfoReturnable<Map<?, ?>> cir) {
        if (! Configs.Optimizations.ASYNC_LITEMATICA_LOAD.getBooleanValue())
            return;

        Map<BlockPos, net.minecraft.nbt.CompoundTag> map = new ConcurrentHashMap<>();
        int threads = Configs.Optimizations.ASYNC_LITEMATICA_LOAD_THREAD_AMOUNT.getIntegerValue();
        var list = new ArrayList<>(oldTE.entrySet());
        ThreadUtils.parallel(list, threads, list.size() / threads, entries -> {
            for (Map.Entry<BlockPos, net.minecraft.nbt.CompoundTag> entry : entries) {
                //? if <=26.2 {
                map.put(entry.getKey(), SchematicConversionMaps.updateBlockEntity(SchematicConversionMaps.checkForIdTag(entry.getValue()), minecraftDataVersion));
                 //?} else {
                /*map.put(entry.getKey(), SchematicConversionMaps.updateBlockEntity(SchematicConversionMaps.checkForIdTag(entry.getValue(), minecraftDataVersion), minecraftDataVersion));
                *///?}
                asa$updateProgress("ConvertTileEntities");
            }
        });

        cir.setReturnValue(map);
    }

    @Inject(
            method = "convertEntities_to_1_20_5",
            at = @At(
                    value = "INVOKE",
                    //? if >= 26.3 {
                    /*target = "Lfi/dy/masa/malilib/util/data/tag/ListData;add(Lfi/dy/masa/malilib/util/data/tag/BaseData;)Z"
                    *///?} else {
                    target = "Lnet/minecraft/nbt/ListTag;add(Ljava/lang/Object;)Z"
                    //?}
                    )
    )
    private void convertEntities_to_1_20_5(CallbackInfoReturnable<net.minecraft.nbt.ListTag> cir) {
        asa$updateProgress("ConvertingEntities");
    }

    @Inject(
            //~ if >= 26.3 'readSubRegionsFromNBT' -> 'readSubRegionsFromData' {
            method = "readSubRegionsFromNBT",
            //~}
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 0
            )
    )
    private void readSubRegionsFromNBT(net.minecraft.nbt.CompoundTag tag, int version,
                                       int minecraftDataVersion, CallbackInfo ci,
                                       @Local(ordinal = 0) BlockPos regionPos, @Local(ordinal = 1) BlockPos regionSize,
                                       @Local(ordinal = 1) net.minecraft.nbt.CompoundTag regionTag,
                                       @Local String regionName) {
        if (regionPos != null && regionSize != null) {
            int total = 0;

            if (version >= 2) {
                //? if >= 1.21.5 < 26.3 {
                total += this.readTileEntitiesFromNBT(regionTag.getListOrEmpty("TileEntities")).size();
                total += regionTag.getListOrEmpty("Entities").size();
                //?} else if < 1.21.5 {
                /*total += this.readTileEntitiesFromNBT(regionTag.getList("TileEntities", fi.dy.masa.malilib.util.data.Constants.NBT.TAG_COMPOUND)).size();
                total += regionTag.getList("Entities", fi.dy.masa.malilib.util.data.Constants.NBT.TAG_COMPOUND).size();
                *///?} else {
                /*total += this.readTileEntitiesFromData(regionTag.getList("TileEntities")).size();
                total += regionTag.getList("Entities").size();
                *///?}
            }
            else if (version == 1) {
                //? if >= 1.21.5 < 26.3 {
                total += this.readTileEntitiesFromNBT_v1(regionTag.getListOrEmpty("TileEntities")).size();
                //?} else if < 1.21.5 {
                /*total += this.readTileEntitiesFromNBT_v1(regionTag.getList("TileEntities", fi.dy.masa.malilib.util.data.Constants.NBT.TAG_COMPOUND)).size();
                *///?} else {
                /*total += this.readTileEntitiesFromData_v1(regionTag.getList("TileEntities")).size();
                *///?}
            }
            asa$curRegion = new RegionInfo(regionName, new AtomicInteger(total), new AtomicInteger(0));
        }
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
//~}
//~}
//?} else {
//@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.asa.utils.DummyClass.class)
//public abstract class LitematicaSchematic_Mixin {
//
//}
//?}

