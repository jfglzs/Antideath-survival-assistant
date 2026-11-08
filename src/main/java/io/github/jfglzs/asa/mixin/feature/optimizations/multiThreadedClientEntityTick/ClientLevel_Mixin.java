package io.github.jfglzs.asa.mixin.feature.optimizations.multiThreadedClientEntityTick;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ClientLevel.class)
public abstract class ClientLevel_Mixin  {
    @Shadow
    protected abstract void tickPassenger(Entity mount, Entity rider);
    @Unique
    private final List<Entity> ENTITY_TICK_LIST = new ArrayList<>();

    @WrapMethod(
            method = "tickNonPassenger"
    )
    public void tickNonPassenger(Entity entity, Operation<Void> original) {
        if (! Configs.DEBUG.getBooleanValue()) {
            original.call(entity);
            return;
        }

        if (entity == MCUtils.getLocalPlayer()) {
            asa$tickNonPassenger(entity);
            return;
        }

        this.ENTITY_TICK_LIST.add(entity);
    }

    @Inject(
            method = "tickEntities",
            at = @At("TAIL")
    )
    public void tickEntities(CallbackInfo ci) {
        if (! Configs.DEBUG.getBooleanValue()) return;

        ThreadUtils.parallel(ENTITY_TICK_LIST, 8, Math.max(ENTITY_TICK_LIST.size() / 8, 1), entities -> {
            for (Entity entity : entities) {
                asa$tickNonPassenger(entity);
            }
        });
        ENTITY_TICK_LIST.clear();
    }

    @Unique
    public void asa$tickNonPassenger(final Entity entity) {
        entity.setOldPosAndRot();
        entity.tickCount++;
        entity.tick();

        for (Entity passenger : entity.getPassengers()) {
            this.tickPassenger(entity, passenger);
        }
    }
}
