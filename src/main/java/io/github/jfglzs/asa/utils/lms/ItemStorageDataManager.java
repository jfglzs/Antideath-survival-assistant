package io.github.jfglzs.asa.utils.lms;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemStorageDataManager {
    private static List<ItemStorage> itemStorages = new ArrayList<>();
    private static final Gson LENIENT_GSON = new GsonBuilder().setLenient().create();
    private static final Type playerType = new TypeToken<List<PlayerItemStorage>>(){}.getType();
    private static final Type itemType = new TypeToken<List<ItemStorage>>(){}.getType();

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (!Configs.LMS_FETCH_SUPPORT.getBooleanValue()) return true;

            String str = message.getString().trim();

            if (str.contains("maxCount") && str.startsWith("{") && str.endsWith("}")) {
                MCUtils.ChatUtils.sendMessOnlyClientVisible("§c请求的数量超出配置的最大上限");
                return false;
            }

            if (str.equals("[]")) {
                MCUtils.ChatUtils.sendMessOnlyClientVisible("§c全无品: 这个物品暂时没有存货");
                return false;
            }

            if (str.contains("id:") && str.contains("count:") && str.startsWith("[{") && str.endsWith("}]")) {
                if (str.contains("name:")) {
                    try {
                        List<PlayerItemStorage> currentList = LENIENT_GSON.fromJson(str, playerType);
                        if (currentList != null && !currentList.isEmpty()) {
                            for (PlayerItemStorage playerItemStorage : currentList) {
                                if (playerItemStorage.name() != null) {
                                    MCUtils.excuteCommand("player " + playerItemStorage.name() + " spawn");
                                    MCUtils.ChatUtils.sendMessOnlyClientVisible("假人: [%s] 取出数量: [%d]".formatted(playerItemStorage.name(), playerItemStorage.count()));
                                }
                            }
                            if (Configs.TEST.getBooleanValue()) {
                                MCUtils.ChatUtils.sendMessOnlyClientVisible(Arrays.toString(currentList.toArray()));
                            }
                        }
                    }
                    catch (Exception e) {
                        AsaMod.LOGGER.error(e.getMessage(), e);
                        MCUtils.ChatUtils.sendMessOnlyClientVisible(e.getMessage());
                    }
                }
                else {
                    try {
                        itemStorages = LENIENT_GSON.fromJson(str, itemType);
                        if (Configs.TEST.getBooleanValue()) {
                            MCUtils.ChatUtils.sendMessOnlyClientVisible(Arrays.toString(itemStorages.toArray()));
                        }
                    }
                    catch (Exception e) {
                        AsaMod.LOGGER.error(e.getMessage(), e);
                        MCUtils.ChatUtils.sendMessOnlyClientVisible(e.getMessage());
                    }
                }
                return false;
            }
            return true;
        });
    }

    public static void submit(Item item, int count) {
        if (item != null) {
            MCUtils.excuteCommand("getItem " + MCUtils.getItemID(item) + " " + count + " " + "nbt");
        }
    }

    public static Text get(ItemStack stack) {
        if (stack.isEmpty()) {
            return Text.empty();
        }

        String stackId = Registries.ITEM.getId(stack.getItem()).toString();

        if (!itemStorages.isEmpty()) {
            for (ItemStorage itemStorage : itemStorages) {
                if (itemStorage.id().equals(stackId)) {
                    int count = itemStorage.count();

                    if (count < 1728) {
                        return Text.of("存货: %s 个".formatted(count)).copy().formatted(Formatting.BOLD, Formatting.GREEN);
                    }
                    else {
                        int devide = 1728;
                        int maxCount = stack.getMaxCount();

                        if (maxCount == 1) {
                            devide = 27;
                        }
                        else if (maxCount == 16) {
                            devide = 432;
                        }

                        return Text.of("存货: %s 个 (%.2f 潜影盒) ".formatted(count, (float) count / devide)).copy().formatted(Formatting.BOLD, Formatting.GREEN);
                    }
                }
            }
        }
        else {
            return Text.of("物品未查询").copy().formatted(Formatting.BOLD, Formatting.RED);
        }

        return Text.of("暂无存货").copy().formatted(Formatting.BOLD, Formatting.RED);
    }

    public static void update() {
        MCUtils.excuteCommand("getStorageData");
    }
}
