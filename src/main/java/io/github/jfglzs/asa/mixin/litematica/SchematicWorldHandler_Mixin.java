package io.github.jfglzs.asa.mixin.litematica;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fi.dy.masa.litematica.event.WorldLoadListener;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldLoadListener.class)
public class SchematicWorldHandler_Mixin {
    @WrapMethod(
            method = "onWorldLoadPost"
    )
    private void asa_onWorldLoadPost(ClientWorld worldBefore, ClientWorld worldAfter, MinecraftClient mc, Operation<Void> original) {
        if (Configs.LITEMATIC_LOAD_OPT.getBooleanValue() && worldAfter != null) {
            AsaMod.LOGGER.info("[ASA] start async task for litematic reloading");
            ThreadUtils.threadPool.submit(() -> original.call(worldBefore, worldAfter, mc));
            MCUtils.ChatUtils.sendMessWithSound("[ASA] 正在异步加载投影文件", SoundEvents.ENTITY_VILLAGER_CELEBRATE, 1f , 1f);
        } else {
            original.call(worldBefore, worldAfter, mc);
        }
    }
}
