//package io.github.jfglzs.asa.mixin.feature.async;
//
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import io.github.jfglzs.asa.config.Configs;
//import io.github.jfglzs.asa.utils.ThreadUtils;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//
//import java.util.function.Consumer;
//
//@Mixin(ClientLevel.class)
//public class ClientLevel_Mixin {
//    @WrapOperation(
//            method = "lambda$tickEntities$0",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;guardEntityTick(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/Entity;)V")
//    )
//    public void tickEntities(ClientLevel instance, Consumer consumer, Entity entity, Operation<Void> original) {
//        if (Configs.DEBUG.getBooleanValue()) {
//            if (entity instanceof Player) {
//                original.call(instance, consumer, entity);
//            }
//            else  {
//                ThreadUtils.runOnTaskThread(() -> original.call(instance, consumer, entity));
//            }
//            return;
//        }
//        original.call(instance, consumer, entity);
//    }
//}
