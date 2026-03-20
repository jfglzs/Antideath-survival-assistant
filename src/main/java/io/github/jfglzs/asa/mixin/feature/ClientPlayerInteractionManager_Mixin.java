package io.github.jfglzs.asa.mixin.feature;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.jfglzs.asa.config.Configs.DISABLE_PLACE_BLOCK_NEARBY_PORTAL;
import static io.github.jfglzs.asa.config.Configs.DISABLE_PLACE_BLOCK_NEARBY_PORTAL_WHITELIST;
import static io.github.jfglzs.asa.utils.MCUtils.getWorld;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManager_Mixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(
            method = "interactBlock",
            at = @At("HEAD") ,
            cancellable = true
    )
    public void interactBlock_Inject(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (DISABLE_PLACE_BLOCK_NEARBY_PORTAL.getBooleanValue()) {
            BlockPos pos = hitResult.getBlockPos();
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = pos.offset(dir);
                BlockState state = getWorld().getBlockState(neighbor);
                Block block = state.getBlock();
                if (block == Blocks.NETHER_PORTAL && !isWhiteList()) cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }

    @Inject(
            method = "interactBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;sendSequencedPacket(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/network/SequencedPacketCreator;)V"
            ),
            cancellable = true
    )
    private void interactBlockInternal_Inject(ClientPlayerEntity player, Hand hand, BlockHitResult result, CallbackInfoReturnable<ActionResult> cir) {
        if (!Configs.PREVENT_INTENTIONAL_GAME_DESIGN.getBooleanValue()) return;

        BlockState state = getWorld().getBlockState(result.getBlockPos());
        RegistryKey<World> worldKey = player.getWorld().getRegistryKey();

        if (worldKey.equals(World.OVERWORLD) || worldKey.equals(World.END)) {
            if (!state.getBlock().equals(Blocks.RESPAWN_ANCHOR)) return;
        }
        if (worldKey.equals(World.NETHER) || worldKey.equals(World.END)) {
            if (!state.isIn(BlockTags.BEDS)) return;
        }

        MCUtils.ChatUtils.sendMessWithSound("§c[ASA]已阻止方块交互", SoundEvents.ENTITY_VILLAGER_DEATH , 1, 1);
        player.swingHand(hand);
        cir.setReturnValue(ActionResult.FAIL);
    }


    @Unique
    private boolean isWhiteList() {
        Item item = client.player.getMainHandStack().getItem();
        for (String id : DISABLE_PLACE_BLOCK_NEARBY_PORTAL_WHITELIST.getStrings()) {
            //? if > 1.20.1 {
            if (id.contains("minecraft")) return false;
            Identifier identifier = Identifier.ofVanilla(id);
            //?} else {
            /*Identifier identifier = new Identifier("minecraft", id);
            *///?}
            Item listedItem = Registries.ITEM.get(identifier);
            if (item.equals(listedItem)) return true;
        }

        return false;
    }
}
