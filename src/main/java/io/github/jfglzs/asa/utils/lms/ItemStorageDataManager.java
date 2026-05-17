package io.github.jfglzs.asa.utils.lms;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.utils.MCUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemStorageDataManager {
    private static final List<ItemStorage> items = new ArrayList<>();
    private static final Gson GSON;
    private static final Type type = new TypeToken<List<ItemStorage>>(){}.getType();

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            String str = message.getString();
            if (str.contains("maxCount") && str.startsWith("{") && str.endsWith("}")) {
                MCUtils.ChatUtils.sendMessOnlyClientVisible("§c请求的数量超出配置的最大上限");
                return false;
            }
            else if (str.contains("name") && str.startsWith("[{") && str.endsWith("}]") && str.contains("count") && str.contains("id")) {
                try {
                    items.addAll(GSON.fromJson(str, type));
                } catch (Exception e) {
                    AsaMod.LOGGER.error(e.getMessage(), e);
                }

                items.forEach(
                        itemStorage -> MCUtils.excuteCommand("player " + itemStorage.name() + " spawn")
                );
                items.clear();
                return false;
            }
            else if (str.contains("[]") && str.startsWith("[") && str.endsWith("]")) {
                MCUtils.ChatUtils.sendMessOnlyClientVisible("§c全无品: 这个物品暂时没有存货");
                return false;
            }
            return true;
        }
        );
    }

    public static void submit(Item item, int count) {
        if (item != null && count != -1) {
            MCUtils.excuteCommand("getItem " + MCUtils.getItemID(item) + " " + count + " " + "nbt");
        }
    }

    static {
        GSON = new Gson();
    }
}
