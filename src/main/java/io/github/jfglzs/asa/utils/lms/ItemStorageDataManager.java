package io.github.jfglzs.asa.utils.lms;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;
import java.util.*;

public class ItemStorageDataManager {
    private static List<ItemStorage> itemStorages = new ArrayList<>();
    private static final Gson LENIENT_GSON = new GsonBuilder().setLenient().create();
    private static final Type playerType = new TypeToken<List<PlayerItemStorage>>(){}.getType();
    private static final Type itemType = new TypeToken<List<ItemStorage>>(){}.getType();
    private static final Set<String> waitForInv = new HashSet<>();
    private static final Set<String> waitForKilling = new HashSet<>();

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (!Configs.LMS_FETCH_SUPPORT.getBooleanValue()) return true;

            var str = message.getString().trim();

            if (str.contains("maxCount:") && str.startsWith("{") && str.endsWith("}")) {
                ChatUtils.sendMessWithSound("§c请求的数量超出配置的最大上限", SoundEvents.VILLAGER_DEATH, 1, 1);
                return false;
            }
            else if (str.startsWith("{") && str.endsWith("}") && str.contains("waitSecond:")) {
                ChatUtils.sendMessWithSound("§c假人取货还在冷却中", SoundEvents.VILLAGER_DEATH, 1, 1);
                return false;
            }
            else if (str.equals("[]")) {
                ChatUtils.sendMessWithSound("§c全无品: 这个物品暂时没有存货", SoundEvents.VILLAGER_NO, 1, 1);
                return false;
            }
            else if (str.contains("id:") && str.contains("count:") && str.startsWith("[{") && str.endsWith("}]")) {
                if (str.contains("name:")) {
                    try {
                        List<PlayerItemStorage> currentList = LENIENT_GSON.fromJson(str, playerType);
                        if (currentList != null && !currentList.isEmpty()) {
                            for (PlayerItemStorage playerItemStorage : currentList) {
                                String name = playerItemStorage.name();
                                if (name != null) {
                                    MCUtils.executeCommand("player %s spawn".formatted(name));
                                    ChatUtils.sendMessOnlyClientVisible("假人: [%s] 取出数量: [%d]".formatted(name, playerItemStorage.count()));
                                    if (Configs.AUTO_OPEN_FAKE_PLAYER_INV.getBooleanValue()) {
                                        waitForInv.add(name);
                                    }
                                    else {
                                        waitForKilling.add(name);
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        AsaMod.LOGGER.error(e.getMessage(), e);
                        ChatUtils.sendMessOnlyClientVisible(e.getMessage());
                    }
                }
                else {
                    try {
                        itemStorages = LENIENT_GSON.fromJson(str, itemType);
                        if (Configs.TEST.getBooleanValue()) {
                            ChatUtils.sendMessOnlyClientVisible(Arrays.toString(itemStorages.toArray()));
                        }
                    }
                    catch (Exception e) {
                        AsaMod.LOGGER.error(e.getMessage(), e);
                        ChatUtils.sendMessOnlyClientVisible(e.getMessage());
                    }
                }
                return false;
            }
            return true;
        });
    }

    public static void submit(Item item, int count) {
        if (item != null) {
            MCUtils.executeCommand("getItem %s %d nbt".formatted(MCUtils.getItemID(item), count));
        }
    }

    public static Component get(ItemStack stack) {
        if (stack.isEmpty()) {
            return Component.empty();
        }

        String stackId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();

        if (!itemStorages.isEmpty()) {
            for (ItemStorage itemStorage : itemStorages) {
                if (itemStorage.id().equals(stackId)) {
                    int count = itemStorage.count();
                    int oneBoxCount = stack.getMaxStackSize() * 27;

                    if (count < oneBoxCount) {
                        return Component.nullToEmpty("存货: %s 个".formatted(count)).copy().withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN);
                    }
                    else {
                        return Component.nullToEmpty("存货: %s 个 (%.2f 潜影盒) ".formatted(count, (float) count / oneBoxCount)).copy().withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN);
                    }
                }
            }
        }
        else {
            return Component.nullToEmpty("物品未查询").copy().withStyle(ChatFormatting.BOLD, ChatFormatting.RED);
        }

        return Component.nullToEmpty("暂无存货").copy().withStyle(ChatFormatting.BOLD, ChatFormatting.RED);
    }

    public static void reflushCache() {
        if (Configs.TEST.getBooleanValue()) ChatUtils.sendMessOnlyClientVisible("Cache reflushed!");
        MCUtils.executeCommand("getStorageData");
    }

    public static void scanMatchedPlayersAndInteract() {
        if (Configs.AUTO_OPEN_FAKE_PLAYER_INV.getBooleanValue()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                for (AbstractClientPlayer player : mc.level.players()) {
                    //~ if >=1.21.10 '.getName()' -> '.name()' {
                    var name = player.getGameProfile().getName();
                    //~}
                    if (waitForInv.remove(name)) {
                        ThreadUtils.runAsync(() -> {
                            try {
                                Thread.sleep(Configs.AUTO_COOLDOWN.getIntegerValue());
                                ThreadUtils.runOnClientThread(() -> {
                                    if (Configs.AUTO_OPEN_FAKE_PLAYER_INV_MODE.getBooleanValue()) {
                                        MCUtils.executeCommand("player %s inventory".formatted(name));
                                    }
                                    else {
                                        player.interact(mc.player, InteractionHand.MAIN_HAND);
                                    }
                                });
                                waitForKilling.add(name);
                            }
                            catch (Exception e) {
                                ChatUtils.sendMessOnlyClientVisible(e.getMessage());
                                AsaMod.LOGGER.error(e.getMessage(), e);
                            }
                        });
                    }
                }
            }
        }
    }

    public static Set<String> getFakePlayerNames() {
        return waitForKilling;
    }
}
