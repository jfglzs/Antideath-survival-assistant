package io.github.jfglzs.asa.mixin.feature;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
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

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameMode_Mixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "useItemOn",
            at = @At("HEAD"),
            cancellable = true
    )
    public void interactBlock_Inject(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (DISABLE_PLACE_BLOCK_NEARBY_PORTAL.getBooleanValue()) {
            BlockPos pos = hitResult.getBlockPos();
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = pos.relative(dir);
                BlockState state = getWorld().getBlockState(neighbor);
                Block block = state.getBlock();
                if (block == Blocks.NETHER_PORTAL && !isWhiteList()) cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }

    @Inject(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startPrediction(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/prediction/PredictiveAction;)V"
            ),
            cancellable = true
    )
    private void interactBlockInternal_Inject(LocalPlayer player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir) {
        if (!Configs.PREVENT_INTENTIONAL_GAME_DESIGN.getBooleanValue()) return;

        BlockState state = getWorld().getBlockState(result.getBlockPos());
        ResourceKey<Level> worldKey = player.level().dimension();

        if (worldKey.equals(Level.OVERWORLD) || worldKey.equals(Level.END)) {
            if (!state.getBlock().equals(Blocks.RESPAWN_ANCHOR)) return;
        }
        if (worldKey.equals(Level.NETHER) || worldKey.equals(Level.END)) {
            if (!state.is(BlockTags.BEDS)) return;
        }

        MCUtils.ChatUtils.sendMessWithSound("已阻止方块交互", SoundEvents.VILLAGER_DEATH, 1, 1);
        player.swing(hand);
        cir.setReturnValue(InteractionResult.FAIL);
    }


    @Unique
    private boolean isWhiteList() {
        Item item = minecraft.player.getMainHandItem().getItem();
        for (String id : DISABLE_PLACE_BLOCK_NEARBY_PORTAL_WHITELIST.getStrings()) {
            //? if > 1.20.1 {
            if (id.contains("minecraft")) return false;
            ResourceLocation identifier = ResourceLocation.withDefaultNamespace(id);
            //?} else {
            /*Identifier identifier = new Identifier("minecraft", id);
             *///?}
            //~ if <=1.21.1 '.getValue(' -> '.get(' {
            Item listedItem = BuiltInRegistries.ITEM.getValue(identifier);
            //~}
            if (item.equals(listedItem)) return true;
        }

        return false;
    }
}
