package io.github.jfglzs.feature;

import io.github.jfglzs.config.Configs;

import java.util.List;

import static io.github.jfglzs.AsaMod.space;
import static io.github.jfglzs.config.Configs.SWITCH_SERVER_COMMAND;
import static io.github.jfglzs.config.Configs.SWITCH_SERVER_LIST;
import static io.github.jfglzs.utils.CommandUtils.runCommand;

public class asaSwitchServer {
    private static int index = 0;

    public static void switchServer(){

        List<String> list = SWITCH_SERVER_LIST.getStrings();
        String[] servers = list.toArray(new String[0]);

        runCommand(SWITCH_SERVER_COMMAND.getStringValue() + space + servers[index++]);
        System.out.println(index);
        if(index == servers.length) index = 0;
    }
}
