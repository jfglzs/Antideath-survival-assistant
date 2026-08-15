package io.github.jfglzs.asa.accessor;

//? if >= 1.21.4 {
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
//?}
public interface IdentifierAccessor {
    //? if >= 1.21.4 {
    void asa$setItemProperties(ClientItem.Properties properties);
    ClientItem.Properties asa$getItemProperties();

    void asa$setItemModel(ItemModel model);
    ItemModel asa$getItemModel();

    int asa$getVersion();
    void asa$setVersion(int version);
    //?}
}
