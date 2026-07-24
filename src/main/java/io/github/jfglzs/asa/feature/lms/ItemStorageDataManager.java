package io.github.jfglzs.asa.feature.lms;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;
import java.util.*;

public class ItemStorageDataManager {
    private static final Map<String, PlayerInventory> PLAYER_INV = new Object2ReferenceArrayMap<>();
    private static final Object2IntMap<String> FAKE_ITEM_STORAGES = new Object2IntArrayMap<>();
    private static final Object2IntMap<String> ITEM_STORAGES = new Object2IntArrayMap<>();
    private static final Gson LENIENT_GSON = new GsonBuilder().setLenient().create();
    private static final Type PLAYER_TYPE = new TypeToken<List<PlayerItemStorage>>() {}.getType();
    private static final Type ITEM_TYPE = new TypeToken<List<ItemStorage>>() {}.getType();
    private static final Set<String> WAIT_FOR_INV = new ObjectArraySet<>();
    private static final Set<String> WAIT_FOR_KILLING = new ObjectArraySet<>();

    public record PlayerInventory(ImmutableList<Slot> slots) {
    }

    record PlayerItemStorage(String name, int count, String id) {
    }

    record ItemStorage(int count, String id) {
    }

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (!Configs.LMS_FETCH_SUPPORT.getBooleanValue()) return true;

            var str = message.getString().trim();

            if (str.contains("maxCount:") && str.startsWith("{") && str.endsWith("}")) {
                ChatUtils.sendMessWithSound(ChatUtils.c("请求的数量超出配置的最大上限").copy().withStyle(ChatFormatting.RED), SoundEvents.VILLAGER_DEATH, 1, 1);
                return false;
            }
            else if (str.startsWith("{") && str.endsWith("}") && str.contains("waitSecond:")) {
                ChatUtils.sendMessWithSound(ChatUtils.c("假人取货还在冷却中").copy().withStyle(ChatFormatting.RED), SoundEvents.VILLAGER_DEATH, 1, 1);
                return false;
            }
            else if (str.equals("[]")) {
                ChatUtils.sendMessWithSound(ChatUtils.c("全无品: 这个物品暂时没有存货").copy().withStyle(ChatFormatting.RED), SoundEvents.VILLAGER_NO, 1, 1);
                return false;
            }
            else if (str.contains("id:") && str.contains("count:") && str.startsWith("[{") && str.endsWith("}]")) {
                if (str.contains("name:")) {
                    try {
                        List<PlayerItemStorage> currentList = LENIENT_GSON.fromJson(str, PLAYER_TYPE);
                        if (currentList != null && !currentList.isEmpty()) {
                            for (PlayerItemStorage itemStorage : currentList) {
                                String name = itemStorage.name();
                                if (name != null) {
                                    MCUtils.executeCommand("player %s spawn".formatted(name));
                                    ChatUtils.sendMessWithSound(ChatUtils.c("假人: [%s] 取出数量: [%d]".formatted(name, itemStorage.count())), SoundEvents.VILLAGER_YES, 1, 1);

                                    if (Configs.AUTO_OPEN_FAKE_PLAYER_INV.getBooleanValue()) {
                                        WAIT_FOR_INV.add(name);
                                    }
                                    else {
                                        WAIT_FOR_KILLING.add(name);
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        AsaMod.LOGGER.error(e.getMessage(), e);
                        ChatUtils.sendMessOnlyClientVisible(ChatUtils.c(e.getMessage()));
                    }
                }
                else {
                    try {
                        ITEM_STORAGES.clear();
                        List<ItemStorage> list = LENIENT_GSON.fromJson(str, ITEM_TYPE);
                        list.forEach(itemStorage -> ITEM_STORAGES.put(itemStorage.id(), itemStorage.count()));
                    }
                    catch (Exception e) {
                        AsaMod.LOGGER.error(e.getMessage(), e);
                        ChatUtils.sendMessOnlyClientVisible(ChatUtils.c(e.getMessage()));
                    }
                }
                return false;
            }
            else if (str.startsWith("[{") && str.endsWith("]") && str.contains("<...>")) {
                ChatUtils.sendMessWithSound(ChatUtils.c("无法通过getStorageData命令查询容器数据 \n 原因: NBT被折叠 \n 请安装Antideath-carpet-addition v1.4.5以上版本并开启 fixNbtFold 规则 \n 或者将LMS 更新至 1.14.1").copy().withStyle(ChatFormatting.RED), SoundEvents.VILLAGER_NO, 1, 1);
                return false;
            }
            return true;
        });
    }

    public static void submit(Item item, int count) {
        if (item == null) return;
        if (Configs.FAKE_PLAYER_INVENTORY_ITEM_CACHE.getBooleanValue()) {
            for (String name : PLAYER_INV.keySet()) {
                PlayerInventory inventory = PLAYER_INV.get(name);
                for (Slot slot : inventory.slots) {
                    if (canSend(slot.getItem(), item)) {
                        MCUtils.executeCommand("player %s spawn".formatted(name));
                        if (Configs.AUTO_OPEN_FAKE_PLAYER_INV.getBooleanValue()) {
                            WAIT_FOR_INV.add(name);
                        }
                        else {
                            WAIT_FOR_KILLING.add(name);
                        }
                        return;
                    }
                }
            }
        }
        if (CommandUtils.canUseCommand("getItem")) {
            MCUtils.executeCommand("getItem %s %d nbt".formatted(MCUtils.getItemID(item), count));
        }
    }

    public static void removeAll() {
        PLAYER_INV.clear();
        ChatUtils.sendOverLayMessage(ChatUtils.c("缓存已清空"));
    }

    public static boolean canSend(ItemStack stack, Item item) {
        if (stack.getItem() == item) return true;
        if (PlayerUtils.isShulkerBox(stack)) {
            List<ItemStack> stacks = PlayerUtils.getBoxItemStacks(stack);
            for (ItemStack boxStack : stacks) {
                if (boxStack.getItem() == item) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void addPlayerInventory(String name, PlayerInventory inventory) {
        PLAYER_INV.put(name, inventory);
        calculateInventory();
    }

    public static void calculateInventory() {
        FAKE_ITEM_STORAGES.clear();
        for (PlayerInventory pi : PLAYER_INV.values()) {
            for (Slot slot : pi.slots) {
                ItemStack stack = slot.getItem();
                String itemID = MCUtils.getItemID(stack.getItem());
                if (PlayerUtils.isShulkerBox(stack)) {
                    for (ItemStack boxStack : PlayerUtils.getBoxItemStacks(stack)) {
                        FAKE_ITEM_STORAGES.merge(MCUtils.getItemID(boxStack.getItem()), boxStack.getCount(), Integer::sum);
                    }
                }
                FAKE_ITEM_STORAGES.merge(itemID, stack.getCount(), Integer::sum);
            }
        }
    }

    public static List<Component> get(ItemStack stack) {
        List<Component> components = new ArrayList<>();
        if (stack.isEmpty()) {
            return components;
        }

        Item item = stack.getItem();
        String itemID = MCUtils.getItemID(stack.getItem());
        int count = getRemainCount(item);

        if (ITEM_STORAGES.isEmpty() && FAKE_ITEM_STORAGES.isEmpty()) {
            components.add(Component.nullToEmpty("物品未查询/缓存").copy().withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
            return components;
        }
        else if (count > 0) {
            int oneBoxCount = stack.getMaxStackSize() * 27;
            if (count < oneBoxCount) {
                components.add(Component.nullToEmpty("存货: %s".formatted(count)).copy().withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN));
            }
            else {
                components.add(Component.nullToEmpty("存货: %d (%.2f 潜影盒) ".formatted(count, (float) count / oneBoxCount)).copy().withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN));
            }
            if (Configs.LITEMATICA_CALCULATE_FAKE.getBooleanValue()) {
                components.add(Component.nullToEmpty("假人存货: %d".formatted(FAKE_ITEM_STORAGES.getInt(itemID))).copy().withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN));
            }
            return components;
        }

        components.add(Component.nullToEmpty("暂无存货").copy().withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
        return components;
    }

    public static int getRemainCount(Item item) {
        String stackId = MCUtils.getItemID(item);
        int count = 0;
        if (Configs.LITEMATICA_CALCULATE_QWP.getBooleanValue()) {
            count = count + ITEM_STORAGES.getInt(stackId);
        }
        if (Configs.LITEMATICA_CALCULATE_FAKE.getBooleanValue()) {
            count = count + FAKE_ITEM_STORAGES.getInt(stackId);
        }
        return count;
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
                    if (WAIT_FOR_INV.remove(name)) {
                        ThreadUtils.runAsync(() -> {
                            try {
                                Thread.sleep(Configs.AUTO_COOLDOWN.getIntegerValue());
                                ThreadUtils.runOnClientThread(() -> MCUtils.executeCommand("player %s inventory".formatted(name))).join();
                                WAIT_FOR_KILLING.add(name);
                            }
                            catch (Exception e) {
                                ChatUtils.sendMessOnlyClientVisible(ChatUtils.c(e.getMessage()));
                                AsaMod.LOGGER.error(e.getMessage(), e);
                            }
                        });
                    }
                }
            }
        }
    }

    public static Set<String> getFakePlayerNames() {
        return WAIT_FOR_KILLING;
    }
}
