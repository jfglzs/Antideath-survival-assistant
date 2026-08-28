package io.github.jfglzs.asa.mixin.feature.optItemModel;

//? if >= 1.21.4 {

import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import org.spongepowered.asm.mixin.Unique;
//?}

import io.github.jfglzs.asa.accessor.IdentifierAccessor;

//~ if >= 1.21.11 'ResourceLocation' -> 'Identifier' {
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Identifier.class)
//~}
public class Identifier_Mixin implements IdentifierAccessor {
    //? if >= 1.21.4 {
    @Unique private ClientItem.Properties asa$properties;
    @Unique private ItemModel asa$itemModel;
    @Unique private int asa$version = 0;

    @Override
    public void asa$setItemProperties(ClientItem.Properties properties) {
        this.asa$properties = properties;
    }

    @Override
    public ClientItem.Properties asa$getItemProperties() {
        return this.asa$properties;
    }

    @Override
    public void asa$setItemModel(ItemModel model) {
        this.asa$itemModel = model;
    }

    @Override
    public ItemModel asa$getItemModel() {
        return this.asa$itemModel;
    }

    @Override
    public int asa$getVersion() {
        return this.asa$version;
    }

    @Override
    public void asa$setVersion(int version) {
        this.asa$version = version;
    }
//?}
}
