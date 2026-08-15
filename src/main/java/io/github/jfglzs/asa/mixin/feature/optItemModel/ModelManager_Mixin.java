package io.github.jfglzs.asa.mixin.feature.optItemModel;

//? if >= 1.21.4 {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.accessor.IdentifierAccessor;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelManager.class)
public class ModelManager_Mixin {
    @Unique
    private int asa$currentVersion = 1;

    @WrapMethod(
            method = "getItemProperties"
    )
    private ClientItem.Properties getItemProperties(Identifier id, Operation<ClientItem.Properties> original) {
        if (Configs.OPT_ITEM_MODEL.getBooleanValue()) {
            IdentifierAccessor accessor = (IdentifierAccessor) (Object) id;
            ClientItem.Properties result = accessor.asa$getItemProperties();
            if (result != null) {
                return result;
            }
            result = original.call(id);
            accessor.asa$setItemProperties(result);
            return result;
        }
        return original.call(id);
    }

    @WrapMethod(
            method = "getItemModel"
    )
    private ItemModel getItemModel(Identifier id, Operation<ItemModel> original) {
        if (Configs.OPT_ITEM_MODEL.getBooleanValue()) {
            IdentifierAccessor accessor = (IdentifierAccessor) (Object) id;
            ItemModel result;
            if (accessor.asa$getVersion() != this.asa$currentVersion || accessor.asa$getItemModel() == null) {
                result = original.call(id);
                accessor.asa$setItemModel(result);
                accessor.asa$setVersion(this.asa$currentVersion);
            }

            result = accessor.asa$getItemModel();
            return result;
        }
        return original.call(id);
    }

    @Inject(
            method = "apply",
            at = @At("TAIL")
    )
    private void apply(CallbackInfo ci) {
        this.asa$currentVersion++;
    }
}
//?}
