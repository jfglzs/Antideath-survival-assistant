package io.github.jfglzs.asa.packet;
//~ if >= 26.1 'ResourceLocation' -> 'Identifier' {
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PlaceEntityPayload(Identifier entityTypeID, double x, double y, double z, float yaw, float pitch) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlaceEntityPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("carpet-igny-addition", "place_entity"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlaceEntityPayload> CODEC = new StreamCodec<>() {
        @Override
        public PlaceEntityPayload decode(RegistryFriendlyByteBuf input) {
            throw new RuntimeException("this shouldn't be called");
        }

        @Override
        public void encode(RegistryFriendlyByteBuf output, PlaceEntityPayload value) {
            output.writeIdentifier(value.entityTypeID());

            output.writeDouble(value.x());
            output.writeDouble(value.y());
            output.writeDouble(value.z());
            output.writeFloat(value.yaw());
            output.writeFloat(value.pitch());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
//~}
