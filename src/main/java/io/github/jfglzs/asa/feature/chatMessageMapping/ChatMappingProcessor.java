package io.github.jfglzs.asa.feature.chatMessageMapping;

import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.CommandUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public class ChatMappingProcessor {
    public static void init() {
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message,
                                                        chatMessage,
                                                        sender,
                                                        type,
                                                        timeStamp
        ) -> {
            if (Configs.CHAT_MESSAGE_MAPPING.getBooleanValue()) {
                try {
                    for (String string : Configs.CHAT_MESSAGE_MAPPING_LIST.getStrings()) {
                        String[] split = string.split("=", 2);
                        String messageString = message.getString();
                        if (messageString.startsWith("<")) {
                            int index = messageString.indexOf("> ");
                            if (index != -1) messageString = messageString.substring(index + 2);
                            if (messageString.startsWith(split[0])) {
                                String command = split[1];
                                if (CommandUtils.canUseCommand(command)) {
                                    MCUtils.executeCommand(command);
                                    return false;
                                }
                            }
                        }
                    }
                }
                catch (Exception e) {
                    AsaMod.LOGGER.error("Error while processing chat message!", e);
                    ChatUtils.sendMessOnlyClientVisible(ChatUtils.toComponent(e.getMessage()));
                }
            }
            return true;
        });
    }
}
