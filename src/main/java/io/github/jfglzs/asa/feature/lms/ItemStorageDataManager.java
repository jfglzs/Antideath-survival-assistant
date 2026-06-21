package io.github.jfglzs.asa.feature.lms;

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
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
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

    record PlayerItemStorage(String name, int count, String id) {
    }

    record ItemStorage(int count, String id) {
    }

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (!Configs.LMS_FETCH_SUPPORT.getBooleanValue()) return true;

            var str = message.getString().trim();

            if (str.contains("maxCount:") && str.startsWith("{") && str.endsWith("}")) {
                ChatUtils.sendMessWithSound(ChatUtils.toComponent("请求的数量超出配置的最大上限").copy().withStyle(ChatFormatting.RED), SoundEvents.VILLAGER_DEATH, 1, 1);
                return false;
            }
            else if (str.startsWith("{") && str.endsWith("}") && str.contains("waitSecond:")) {
                ChatUtils.sendMessWithSound(ChatUtils.toComponent("假人取货还在冷却中").copy().withStyle(ChatFormatting.RED), SoundEvents.VILLAGER_DEATH, 1, 1);
                return false;
            }
            else if (str.equals("[]")) {
                ChatUtils.sendMessWithSound(ChatUtils.toComponent("全无品: 这个物品暂时没有存货").copy().withStyle(ChatFormatting.RED), SoundEvents.VILLAGER_NO, 1, 1);
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
                                    ChatUtils.sendMessWithSound(ChatUtils.toComponent("假人: [%s] 取出数量: [%d]".formatted(name, playerItemStorage.count())), SoundEvents.VILLAGER_YES, 1, 1);

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
                        ChatUtils.sendMessOnlyClientVisible(ChatUtils.toComponent(e.getMessage()));
                    }
                }
                else {
                    try {
                        itemStorages = LENIENT_GSON.fromJson(str, itemType);
                        if (Configs.TEST.getBooleanValue()) {
                            ChatUtils.sendMessOnlyClientVisible(ChatUtils.toComponent(Arrays.toString(itemStorages.toArray())));
                        }
                    }
                    catch (Exception e) {
                        AsaMod.LOGGER.error(e.getMessage(), e);
                        ChatUtils.sendMessOnlyClientVisible(ChatUtils.toComponent(e.getMessage()));
                    }
                }
                return false;
            }
            else if (str.startsWith("[{") && str.endsWith("]") && str.contains("<...>")) {
                ChatUtils.sendMessWithSound(ChatUtils.toComponent("无法通过getStorageData命令查询容器数据 \n 原因: NBT被折叠 \n 请安装Antideath-carpet-addition v1.4.5以上版本并开启 fixNbtFold 规则 \n 或者将LMS 更新至 1.15").copy().withStyle(ChatFormatting.RED), SoundEvents.VILLAGER_NO, 1, 1);
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

        String stackId = MCUtils.getItemID(stack.getItem());

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
        MCUtils.executeCommand("getStorageData");
    }

    public static void scanMatchedPlayersAndInteract(Minecraft mc) {
        if (Configs.AUTO_OPEN_FAKE_PLAYER_INV.getBooleanValue()) {
            if (mc.level != null) {
                for (AbstractClientPlayer player : mc.level.players()) {
                    //~ if >=1.21.10 '.getName()' -> '.name()' {
                    var name = player.getGameProfile().name();
                    //~}
                    if (waitForInv.remove(name)) {
                        ThreadUtils.runAsync(() -> {
                            try {
                                Thread.sleep(Configs.AUTO_COOLDOWN.getIntegerValue());
                                ThreadUtils.runOnClientThread(() -> MCUtils.executeCommand("player %s inventory".formatted(name))).join();
                                waitForKilling.add(name);
                            }
                            catch (Exception e) {
                                ChatUtils.sendMessOnlyClientVisible(ChatUtils.toComponent(e.getMessage()));
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
