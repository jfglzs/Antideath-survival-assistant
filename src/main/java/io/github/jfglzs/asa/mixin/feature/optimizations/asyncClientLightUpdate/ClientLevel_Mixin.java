package io.github.jfglzs.asa.mixin.feature.optimizations.asyncClientLightUpdate;

//? if >= 26.1 {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientLevel.class)
public class ClientLevel_Mixin {
    @WrapMethod(
            method = "update"
    )
    public void update(Operation<Void> original) {
        if (! Configs.Disables.DISABLE_CLIENT_LIGHT_UPDATE.getBooleanValue())
            original.call();
    }
}
//?} else {
/*@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.asa.utils.DummyClass.class)
public class ClientLevel_Mixin {}
*///?}
