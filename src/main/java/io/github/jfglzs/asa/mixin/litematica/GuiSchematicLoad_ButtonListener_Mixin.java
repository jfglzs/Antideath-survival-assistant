package io.github.jfglzs.asa.mixin.litematica;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fi.dy.masa.litematica.gui.GuiSchematicLoad;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ThreadUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.concurrent.Future;

@Mixin(targets = "fi.dy.masa.litematica.gui.GuiSchematicLoad$ButtonListener")
public class GuiSchematicLoad_ButtonListener_Mixin {
    //#if MC > 12001
    @Shadow @Final
    private GuiSchematicLoad gui;

    @Unique
    private Future<Void> future;

    @WrapMethod(
            method = "actionPerformedWithButton"
    )
    public void litematicLoadOpt_Warp(ButtonBase button, int mouseButton, Operation<Void> original) {
        boolean bl = Configs.LITEMATIC_LOAD_OPT.getBooleanValue();
        if (bl && future == null || bl && future.isDone()) {
            AsaMod.LOGGER.info("[ASA] Start Async task for litematic loading");
            gui.addMessage(Message.MessageType.INFO, "[ASA] 开始异步加载原理图，此操作可能花费数分钟时间，请耐心等待");
            future = ThreadUtils.threadPool.submit(() -> original.call(button, mouseButton));
        } else if (!bl) {
            original.call(button, mouseButton);
        } else {
            gui.addMessage(Message.MessageType.WARNING, "[ASA] 请等待当前任务加载完成");
        }
    }
    //#endif
}