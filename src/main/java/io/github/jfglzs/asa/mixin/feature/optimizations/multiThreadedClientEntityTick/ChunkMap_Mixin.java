package io.github.jfglzs.asa.mixin.feature.optimizations.multiThreadedClientEntityTick;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ChunkMap.class)
public class ChunkMap_Mixin {
    @Shadow
    @Final
    @Mutable
    private Int2ObjectMap<?> entityMap;

    public ChunkMap_Mixin() {
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void replaceConVars(CallbackInfo ci) {
        entityMap = Int2ObjectMaps.synchronize(new Int2ObjectArrayMap<>());
    }

    @WrapMethod(method = "addEntity")
    private synchronized void addEntity(Entity entity, Operation<Void> original) {
        original.call(entity);
    }

    @WrapMethod(method = "removeEntity")
    private synchronized void removeEntity(Entity entity, Operation<Void> original) {
        original.call(entity);
    }

    @WrapMethod(method = "releaseGeneration")
    private synchronized void releaseGeneration(GenerationChunkHolder chunkHolder, Operation<Void> original) {
        original.call(chunkHolder);
    }

    @WrapMethod(method = "setChunkUnsaved")
    private void setChunkUnsaved(ChunkPos chunkPos, Operation<Void> original) {
        synchronized (this) {
            original.call(chunkPos);
        }
    }

    @WrapMethod(method = "saveChunksEagerly")
    private void saveChunksEagerly(BooleanSupplier haveTime, Operation<Void> original) {
        synchronized (this) {
            original.call(haveTime);
        }
    }

    @WrapMethod(method = "saveAllChunks")
    private void saveAllChunks(boolean flushStorage, Operation<Void> original) {
        synchronized (this) {
            original.call(flushStorage);
        }
    }

    @Inject(method = "addEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;pauseInIde(Ljava/lang/Throwable;)Ljava/lang/Throwable;"), cancellable = true)
    private void skipThrowLoadEntity(Entity entity, CallbackInfo ci) {
        ci.cancel();
    }
}
