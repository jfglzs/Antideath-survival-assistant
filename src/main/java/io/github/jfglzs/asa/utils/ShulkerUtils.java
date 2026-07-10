package io.github.jfglzs.asa.utils;

import net.kyrptonaught.quickshulker.network.OpenShulkerPacket;

public class ShulkerUtils {
    public static void open(int index) {
        if (Mods.isShulkerBoxLoaded) {
            OpenShulkerPacket.sendOpenPacket(index);
        }
        else {
            ChatUtils.sendOverLayMessage(ChatUtils.toComponent("[ASA]未安装快捷潜影盒"));
        }
    }
}
