package io.github.jfglzs.asa.mixin.feature.optimizations.asyncLitematicLoad;

//? if > 1.21.1 {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.gui.GuiMaterialList;
import fi.dy.masa.litematica.gui.GuiSchematicLoad;
import fi.dy.masa.litematica.materials.MaterialListBase;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(targets = "fi.dy.masa.litematica.gui.GuiSchematicLoad$ButtonListener")
public class GuiSchematicLoad_Mixin {
    @Final
    @Shadow
    private GuiSchematicLoad gui;
    @Unique
    private static CompletableFuture<Void> asa$future;

    @WrapMethod(
            method = "actionPerformedWithButton"
    )
    public void actionPerformedWithButton(ButtonBase button, int mouseButton, Operation<Void> original) {
        if (! Configs.Optimizations.ASYNC_LITEMATICA_LOAD.getBooleanValue()) {
            original.call(button, mouseButton);
            return;
        }

        if (asa$future == null || asa$future.isDone()) {
            asa$future = CompletableFuture.supplyAsync(() -> original.call(button, mouseButton));
            asa$future.whenComplete((v, e) -> {
                if (e != null) {
                    this.gui.addMessage(Message.MessageType.ERROR, "asa.asyncLitematicaLoad.err", e.getMessage());
                    ChatUtils.actionBar(Component.translatable("asa.asyncLitematicaLoad.err", e.getMessage()));
                    AsaMod.LOGGER.error("Error when load async", e);
                }
                else
                    ChatUtils.actionBar(Component.translatable("asa.asyncLitematicaLoad.complete"));
            });
        }
        else {
            this.gui.addMessage(Message.MessageType.ERROR, "asa.asyncLitematicaLoad.working");
        }
    }

    //真不是我想这样写 之前有想过更好的方案 ( 异步线程datafix 最后在渲染线程ori.call() ) 不过那套方案有时候能加载 有时候无法加载 也只能这样了了 🤣
    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/gui/GuiSchematicLoad;addMessage(Lfi/dy/masa/malilib/gui/Message$MessageType;Ljava/lang/String;[Ljava/lang/Object;)V"
            )
    )
    public void syncAddMessage(GuiSchematicLoad instance, Message.MessageType messageType, String s, Object[] objects,
                           Operation<Void> original) {
        ThreadUtils.runOnClientThread(() -> original.call(instance, messageType, s, objects));
    }

    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/GuiBase;openGui(Lnet/minecraft/client/gui/screens/Screen;)V"
            )
    )
    public void openGui(Screen gui, Operation<Void> original) {
        ThreadUtils.runOnClientThread(() -> original.call(gui));
    }

    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/data/SchematicHolder;addSchematic(Lfi/dy/masa/litematica/schematic/LitematicaSchematic;Z)V"
            )
    )
    public void addSchematic(SchematicHolder instance, LitematicaSchematic schematic, boolean allowDuplicates,
                             Operation<Void> original) {
        ThreadUtils.runOnClientThread(() -> original.call(instance, schematic, allowDuplicates));
    }

    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/schematic/placement/SchematicPlacementManager;addSchematicPlacement(Lfi/dy/masa/litematica/schematic/placement/SchematicPlacement;Z)V"
            )
    )
    public void addSchematicPlacement(SchematicPlacementManager instance, SchematicPlacement placement,
                                      boolean printMessages, Operation<Void> original) {
        ThreadUtils.runOnClientThread(() -> original.call(instance, placement, printMessages));
    }

    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/schematic/placement/SchematicPlacementManager;setSelectedSchematicPlacement(Lfi/dy/masa/litematica/schematic/placement/SchematicPlacement;)V"
            )
    )
    public void setSelectedSchematicPlacement(SchematicPlacementManager instance, SchematicPlacement placement,
                                              Operation<Void> original) {
        ThreadUtils.runOnClientThread(() -> original.call(instance, placement));
    }

    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "NEW",
                    target = "fi/dy/masa/litematica/gui/GuiMaterialList"
            )
    )
    public GuiMaterialList GuiMaterialList(MaterialListBase materialList, Operation<GuiMaterialList> original) {
        return ThreadUtils.runOnClientThread(() -> original.call(materialList));
    }

    @Inject(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "FIELD",
                    target = "Lfi/dy/masa/litematica/util/FileType;LITEMATICA_SCHEMATIC:Lfi/dy/masa/litematica/util/FileType;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    public void actionPerformedWithButton(ButtonBase button, int mouseButton, CallbackInfo ci) {
        if (Configs.Optimizations.ASYNC_LITEMATICA_LOAD.getBooleanValue()) {
            this.gui.addMessage(Message.MessageType.INFO, "asa.asyncLitematicaLoad.start");
        }
    }
}
//?} else {
//@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.asa.utils.DummyClass.class)
//public class GuiSchematicLoad_Mixin {}
//?}



