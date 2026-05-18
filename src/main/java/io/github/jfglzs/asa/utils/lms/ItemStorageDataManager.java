package io.github.jfglzs.asa.utils.lms;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.jfglzs.asa.AsaMod;
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
import java.util.List;

public class ItemStorageDataManager {
    private static final List<PlayerItemStorage> playerItemStorages = new ArrayList<>();
    private static List<ItemStorage> itemStorages = new ArrayList<>();
    private static final Gson GSON = new Gson();
    private static final Gson LENIENT_GSON = new GsonBuilder().setLenient().create();
    private static final Type playerType = new TypeToken<List<PlayerItemStorage>>(){}.getType();
    private static final Type itemType = new TypeToken<List<ItemStorage>>(){}.getType();

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            String str = message.getString();
            if (str.contains("maxCount") && str.startsWith("{") && str.endsWith("}")) {
                MCUtils.ChatUtils.sendMessOnlyClientVisible("§c请求的数量超出配置的最大上限");
                return false;
            }
            else if (str.contains("name") && str.startsWith("[{") && str.endsWith("}]") && str.contains("count") && str.contains("id")) {
                try {
                    playerItemStorages.addAll(GSON.fromJson(str, playerType));
                }
                catch (Exception e) {
                    AsaMod.LOGGER.error(e.getMessage(), e);
                }

                playerItemStorages.forEach(
                        playerItemStorage -> {
                            MCUtils.excuteCommand("player " + playerItemStorage.name() + " spawn");
                            MCUtils.ChatUtils.sendMessOnlyClientVisible("假人:%s 取出数量:%d ".formatted(playerItemStorage.name(), playerItemStorage.count()));
                        }
                );

                playerItemStorages.clear();
                return false;
            }
            else if (str.contains("[]") && str.startsWith("[") && str.endsWith("]") && !str.contains("count")) {
                MCUtils.ChatUtils.sendMessOnlyClientVisible("§c全无品: 这个物品暂时没有存货");
                return false;
            }
            else if (str.startsWith("[{") && str.endsWith("}]") && str.contains("count") && str.contains("id")) {
                try {
                    itemStorages = LENIENT_GSON.fromJson(str, itemType);
                }
                catch (Exception e) {
                    AsaMod.LOGGER.error(e.getMessage(), e);
                }
                return false;
            }

            return true;
        });
    }

    public static void submit(Item item, int count) {
        if (item != null && count != -1) {
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

                    if (count > 0 && count < 1728) {
                        return Text.of("存货: %s 个".formatted(count)).copy().formatted(Formatting.BOLD, Formatting.GREEN);
                    } else if (count > 1728) {
                        int devide = 1728;
                        if (stack.getMaxCount() == 1) {
                            devide = 1;
                        }
                        else if (stack.getMaxCount() == 16) {
                            devide = 432;
                        }
                        return Text.of("存货: %s 个 (%.2f 潜影盒) ".formatted(count, (float) count / devide)).copy().formatted(Formatting.BOLD, Formatting.GREEN);
                    }
                }
            }
        } else {
            return Text.of("物品未查询").copy().formatted(Formatting.BOLD, Formatting.RED);
        }

        return Text.of("暂无存货").copy().formatted(Formatting.BOLD, Formatting.RED);
    }

    public static void update() {
        MCUtils.excuteCommand("getStorageData");
    }
}
