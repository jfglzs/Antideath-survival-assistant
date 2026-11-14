package io.github.jfglzs.asa.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.jfglzs.asa.AsaMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UpdateChecker {
    private static final HttpClient CLIENT = HttpClient.newBuilder().executor(ThreadUtils.THREAD_POOL).connectTimeout(Duration.ofSeconds(5)).build();
    private static final URI VERSIONS_URL = URI.create("https://api.github.com/repos/jfglzs/Antideath-survival-assistant/releases/latest");
    private static final URI DOWNLOAD_URL = URI.create("https://modrinth.com/mod/antideath-survival-assistant");
    private static boolean firstCheck = true;
    public static boolean hasUpdate;
    public static String versionLastest;

    public static void checkForUpdates() {
        try {
            HttpRequest request = HttpRequest.newBuilder(VERSIONS_URL).GET().build();
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200)
                return;

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            String latestVersion = json.get("tag_name").getAsString();
            String currentVersion = AsaMod.version;

            int lv = toInt(latestVersion);
            int cv = toInt(currentVersion);

            if (lv > cv) {
                hasUpdate = true;
                firstCheck = false;
                versionLastest = latestVersion;
                AsaMod.LOGGER.info("AsaMod v{} is ready!", versionLastest);
                sendUpdateMessage();
            }
        }
        catch (Exception e) {
            AsaMod.LOGGER.error("error while checking for updates", e);
        }
    }

    public static void sendUpdateMessage() {
        if (! firstCheck) {
            if (hasUpdate) {
                MCUtils.executeTask(() -> {
                    MutableComponent component = ChatHelper.c(Component.translatable("asa.checkupdate.available", versionLastest, AsaMod.version)).copy();
                    Style style = component.getStyle();
                    //? if >=1.21.5 {
                    style = style.withClickEvent(new ClickEvent.OpenUrl(DOWNLOAD_URL))
                    //?} else {
                    //style = style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, DOWNLOAD_URL.toString()))
                    //?}
                                 .applyFormat(ChatFormatting.GOLD).applyFormat(ChatFormatting.UNDERLINE).applyFormat(ChatFormatting.BOLD);
                    component.setStyle(style);
                    ChatHelper.clientChat(component);
                });
            }
        }
        else {
            ThreadUtils.THREAD_POOL.execute(UpdateChecker::checkForUpdates);
        }
    }

    public static int toInt(String v) {
        return Integer.parseInt(v.replaceAll("[^0-9]", ""));
    }
}
