package io.github.jfglzs.asa.feature.switchserver;

import io.github.jfglzs.asa.utils.MCUtils;

import java.util.List;

import static io.github.jfglzs.asa.AsaMod.LOGGER;
import static io.github.jfglzs.asa.AsaMod.SPACE;
import static io.github.jfglzs.asa.config.Configs.SWITCH_SERVER_COMMAND;
import static io.github.jfglzs.asa.config.Configs.SWITCH_SERVER_LIST;

public class asaSwitchServer {
    private static int index = 0;

    public static void switchServer(){

        List<String> list = SWITCH_SERVER_LIST.getStrings();
        String[] servers = list.toArray(new String[0]);

        if(servers.length == 0) {
            MCUtils.ChatUtils.sendMessOnlyClientVisible("§c服务器列表不能为空,请前往设置菜单进行设置");
            return;
        }

        MCUtils.excuteCommand(SWITCH_SERVER_COMMAND.getStringValue() + SPACE + servers[index++]);
        LOGGER.info("Switch to {}", servers[index]);
        if(index == servers.length) index = 0;
    }

    public static void switchServer(String Server) {
        if (Server.isEmpty()) {
            MCUtils.ChatUtils.sendMessOnlyClientVisible("§c服务器名称不能为空,请前往设置菜单进行设置");
            return;
        }
        MCUtils.excuteCommand(SWITCH_SERVER_COMMAND.getStringValue() + SPACE + Server);
    }
}
