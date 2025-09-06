package io.github.jfglzs.feature;

import java.util.Arrays;
import java.util.List;

import static io.github.jfglzs.AsaMod.SPACE;
import static io.github.jfglzs.config.Configs.SWITCH_SERVER_COMMAND;
import static io.github.jfglzs.config.Configs.SWITCH_SERVER_LIST;
import static io.github.jfglzs.utils.ChatUtils.sendChatOnlyClientVisible;
import static io.github.jfglzs.utils.CommandUtils.runCommand;

public class asaSwitchServer
{
    private static int index = 0;

    public static void switchServer(){

        List<String> list = SWITCH_SERVER_LIST.getStrings();
        String[] servers = list.toArray(new String[0]);

        if(Arrays.toString(servers).equals("[]"))
        {
            sendChatOnlyClientVisible("§c服务器列表不能为空,请前往设置菜单进行设置");
            return;
        }

        runCommand(SWITCH_SERVER_COMMAND.getStringValue() + SPACE + servers[index++]);
        System.out.println(index);
        if(index == servers.length) index = 0;
    }
}
