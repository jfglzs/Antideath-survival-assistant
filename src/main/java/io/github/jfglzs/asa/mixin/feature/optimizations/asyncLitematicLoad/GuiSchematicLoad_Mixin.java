package io.github.jfglzs.asa.mixin.feature.optimizations.asyncLitematicLoad;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.litematica.gui.GuiSchematicLoad;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase;
import fi.dy.masa.malilib.interfaces.IStringConsumer;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.mixin.invokers.GuiListBase_Invoker;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Mixin(targets = "fi.dy.masa.litematica.gui.GuiSchematicLoad$ButtonListener")
public class GuiSchematicLoad_Mixin {
    @Shadow
    @Final
    private GuiSchematicLoad gui;
    @Unique
    private static CompletableFuture<LitematicaSchematic> futureSchematic;
    @Unique
    private static LitematicaSchematic schematic;

    @WrapMethod(
            method = "actionPerformedWithButton"
    )
    public void actionPerformedWithButton(ButtonBase button, int mouseButton, Operation<Void> original) {
        GuiListBase_Invoker invoker = (GuiListBase_Invoker) this.gui;

        if (! Configs.Optimizations.ASYNC_LITEMATICA_LOAD.getBooleanValue() || invoker.asa$getListWidget() == null) {
            original.call(button, mouseButton);
            return;
        }

        var entry = (WidgetFileBrowserBase.DirectoryEntry) invoker.asa$getListWidget().getLastSelectedEntry();

        if (entry == null) {
            original.call(button, mouseButton);
            return;
        }

        Path file = entry.getFullPath();
        FileType fileType = FileType.fromFile(entry.getFullPath());

        if (! Files.exists(file) || !Files.isReadable(file) || fileType == FileType.JSON || fileType == FileType.TEXT) {
            original.call(button, mouseButton);
            return;
        }

        if (futureSchematic != null) {
            this.gui.addMessage(Message.MessageType.ERROR, "asa.asyncLitematicaLoad.working");
            return;
        }

        this.gui.addMessage(Message.MessageType.INFO, "asa.asyncLitematicaLoad.start");

        futureSchematic = CompletableFuture.supplyAsync(() -> {
            String name = entry.name();
            Path directory = entry.getDirectory();

            if (fileType == FileType.LITEMATICA_SCHEMATIC)
                return LitematicaSchematic.createFromFile(directory, name);
            else if (fileType == FileType.SCHEMATICA_SCHEMATIC)
                return WorldUtils.convertSchematicaSchematicToLitematicaSchematic(directory, name, false, this.gui);
            else if (fileType == FileType.VANILLA_STRUCTURE)
                return WorldUtils.convertStructureToLitematicaSchematic(directory, name);
            else if (fileType == FileType.SPONGE_SCHEMATIC)
                return WorldUtils.convertSpongeSchematicToLitematicaSchematic(directory, name);

            return null;
        }, ThreadUtils.THREAD_POOL);

        futureSchematic.thenAccept(result -> ThreadUtils.runOnClientThread(() -> {
            schematic = result;
            original.call(button, mouseButton);
            schematic = null;
            futureSchematic = null;
            ChatUtils.actionBar(Component.translatable("asa.asyncLitematicaLoad.complete"));

        }));
    }

    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/schematic/LitematicaSchematic;createFromFile(Ljava/nio/file/Path;Ljava/lang/String;)Lfi/dy/masa/litematica/schematic/LitematicaSchematic;"
            )
    )
    private static LitematicaSchematic createFromFile(Path dir, String fileName,
                                                      Operation<LitematicaSchematic> original) {
        if (schematic != null)
            return schematic;
        return original.call(dir, fileName);
    }

    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/util/WorldUtils;convertStructureToLitematicaSchematic(Ljava/nio/file/Path;Ljava/lang/String;)Lfi/dy/masa/litematica/schematic/LitematicaSchematic;"
            )
    )
    private static LitematicaSchematic convertStructureToLitematicaSchematic(Path dir, String fileName,
                                                      Operation<LitematicaSchematic> original) {
        if (schematic != null)
            return schematic;
        return original.call(dir, fileName);
    }

    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/util/WorldUtils;convertSpongeSchematicToLitematicaSchematic(Ljava/nio/file/Path;Ljava/lang/String;)Lfi/dy/masa/litematica/schematic/LitematicaSchematic;"
            )
    )
    private static LitematicaSchematic convertSpongeSchematicToLitematicaSchematic(Path dir, String fileName,
                                                      Operation<LitematicaSchematic> original) {
        if (schematic != null)
            return schematic;
        return original.call(dir, fileName);
    }

    @WrapOperation(
            method = "actionPerformedWithButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/util/WorldUtils;convertSchematicaSchematicToLitematicaSchematic(Ljava/nio/file/Path;Ljava/lang/String;ZLfi/dy/masa/malilib/interfaces/IStringConsumer;)Lfi/dy/masa/litematica/schematic/LitematicaSchematic;"
            )
    )
    private static LitematicaSchematic convertSchematicaSchematicToLitematicaSchematic(Path inputDir,
                                                                                      String inputFileName,
                                                                                      boolean ignoreEntities,
                                                                                      IStringConsumer feedback,
                                                                                      Operation<LitematicaSchematic> original) {
        if (schematic != null)
            return schematic;
        return original.call(inputDir, inputFileName, ignoreEntities, feedback);
    }
}
