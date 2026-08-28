package io.github.jfglzs.asa.mixin.feature.optimizations.optItemFrame;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.asa.accessor.IClientPacketListenerAccessor1;
import io.github.jfglzs.asa.config.Configs;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListener_Mixin implements IClientPacketListenerAccessor1 {
    @Unique private final Int2IntArrayMap ASA$MAPS = new Int2IntArrayMap();

    @Inject(
            method = "handleRespawn",
            at = @At("HEAD")
    )
    public void handleRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
        this.ASA$MAPS.clear();
    }


    @Inject(
            method = "handleMapItemData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;applyToMap(Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;)V"
            ),
            cancellable = true
    )
    public void handleMapItemData(ClientboundMapItemDataPacket packet, CallbackInfo ci, @Local MapId id,
                                  @Local MapItemSavedData savedData) {
        if (Configs.OPT_ITEM_FRAME.getBooleanValue()) {
            int saveDataHash = savedData.hashCode();
            int intID = id.id();
            if (intID != 0 && this.ASA$MAPS.get(intID) == saveDataHash) {
                ci.cancel();
            }
            else {
                this.ASA$MAPS.put(intID, saveDataHash);
            }
        }
    }

    @Override
    public Int2IntArrayMap asa$getMaps() {
        return this.ASA$MAPS;
    }
}

