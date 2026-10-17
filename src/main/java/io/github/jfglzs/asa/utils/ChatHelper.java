package io.github.jfglzs.asa.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvent;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatHelper {
    private static final Minecraft client = Minecraft.getInstance();
    private static final Pattern colorExtract = Pattern.compile("#([0-9a-fA-F]{6})");

    //from carpet mod Messenger$CarpetFormatting
    public enum CarpetFormatting {
        ITALIC('i', (s, f) -> s.withItalic(true)),
        STRIKE('s', (s, f) -> s.applyFormat(ChatFormatting.STRIKETHROUGH)),
        UNDERLINE('u', (s, f) -> s.applyFormat(ChatFormatting.UNDERLINE)),
        BOLD('b', (s, f) -> s.withBold(true)),
        OBFUSCATE('o', (s, f) -> s.applyFormat(ChatFormatting.OBFUSCATED)),

        WHITE('w', (s, f) -> s.withColor(ChatFormatting.WHITE)),
        YELLOW('y', (s, f) -> s.withColor(ChatFormatting.YELLOW)),
        LIGHT_PURPLE('m', (s, f) -> s.withColor(ChatFormatting.LIGHT_PURPLE)), // magenta
        RED('r', (s, f) -> s.withColor(ChatFormatting.RED)),
        AQUA('c', (s, f) -> s.withColor(ChatFormatting.AQUA)), // cyan
        GREEN('l', (s, f) -> s.withColor(ChatFormatting.GREEN)), // lime
        BLUE('t', (s, f) -> s.withColor(ChatFormatting.BLUE)), // light blue, teal
        DARK_GRAY('f', (s, f) -> s.withColor(ChatFormatting.DARK_GRAY)),
        GRAY('g', (s, f) -> s.withColor(ChatFormatting.GRAY)),
        GOLD('d', (s, f) -> s.withColor(ChatFormatting.GOLD)),
        DARK_PURPLE('p', (s, f) -> s.withColor(ChatFormatting.DARK_PURPLE)), // purple
        DARK_RED('n', (s, f) -> s.withColor(ChatFormatting.DARK_RED)),  // brown
        DARK_AQUA('q', (s, f) -> s.withColor(ChatFormatting.DARK_AQUA)),
        DARK_GREEN('e', (s, f) -> s.withColor(ChatFormatting.DARK_GREEN)),
        DARK_BLUE('v', (s, f) -> s.withColor(ChatFormatting.DARK_BLUE)), // navy
        BLACK('k', (s, f) -> s.withColor(ChatFormatting.BLACK)),

        COLOR('#', (s, f) -> {
            TextColor color;
            try {
                color = TextColor.parseColor("#" + f).getOrThrow(RuntimeException::new);
            }
            catch (RuntimeException e) {
                return s;
            }
            return s.withColor(color);
        }, s -> {
            Matcher m = colorExtract.matcher(s);
            return m.find() ? m.group(1) : null;
        });

        public final char code;
        public final BiFunction<Style, String, Style> applier;
        public final Function<String, String> container;

        CarpetFormatting(char code, BiFunction<Style, String, Style> applier) {
            this(code, applier, s -> s.indexOf(code) >= 0 ? Character.toString(code) : null);
        }

        CarpetFormatting(char code, BiFunction<Style, String, Style> applier, Function<String, String> container) {
            this.code = code;
            this.applier = applier;
            this.container = container;
        }

        public Style apply(String format, Style previous) {
            String fmt;
            if ((fmt = container.apply(format)) != null)
                return applier.apply(previous, fmt);
            return previous;
        }
    }

    public static void clientMess(Component chat) {
        if (client.player == null)
            return;
        //? if >=26.1 {
        client.player.sendSystemMessage(chat);
        //?} else {
        /*client.player.displayClientMessage(chat, false);
         *///?}
    }

    public static void clientMesswithSound(Component chat, SoundEvent type) {
        LocalPlayer player = client.player;
        if (player != null) {
            player.playSound(type, 1f, 1f);
            clientMess(chat);
        }
    }

    public static void actionBarWithSound(Component chat, SoundEvent type) {
        LocalPlayer player = client.player;
        if (player == null)
            return;
        player.playSound(type, 1f, 1f);
        actionBar(chat);
    }

    public static void actionBar(Component chat) {
        LocalPlayer player = client.player;
        if (player == null)
            return;
        //? if >=26.1 {
        player.sendOverlayMessage(chat);
        //?} else {
        /*player.displayClientMessage(chat, true);
         *///?}
    }

    public static void serverMess(String chat) {
        if (client.player != null) {
            client.player.connection.sendChat(chat);
        }
    }

    //from carpet mod Messenger#c
    public static Component c(Object... fields) {
        MutableComponent message = Component.literal("[ASA] ");
        MutableComponent previousComponent = null;
        for (Object o : fields) {
            if (o instanceof MutableComponent) {
                message.append((MutableComponent) o);
                previousComponent = (MutableComponent) o;
                continue;
            }
            String txt = o.toString();
            MutableComponent comp = getChatComponentFromDesc(txt, previousComponent);
            if (comp != previousComponent)
                message.append(comp);
            previousComponent = comp;
        }
        return message;
    }

    //from carpet mod Messenger#getChatComponentFromDesc
    private static MutableComponent getChatComponentFromDesc(String message, MutableComponent previousMessage) {
        if (message.equalsIgnoreCase("")) {
            return Component.literal("");
        }
        if (Character.isWhitespace(message.charAt(0))) {
            message = "w" + message;
        }
        int limit = message.indexOf(' ');
        String desc = message;
        String str = "";
        if (limit >= 0) {
            desc = message.substring(0, limit);
            str = message.substring(limit + 1);
        }
        if (previousMessage == null) {
            MutableComponent text = Component.literal(str);
            text.setStyle(parseStyle(desc));
            return text;
        }
        return previousMessage;
    }

    //from carpet mod Messenger#parseStyle
    public static Style parseStyle(String style) {
        Style myStyle = Style.EMPTY.withColor(ChatFormatting.WHITE);
        for (CarpetFormatting cf : CarpetFormatting.values())
            myStyle = cf.apply(style, myStyle);
        return myStyle;
    }
}
