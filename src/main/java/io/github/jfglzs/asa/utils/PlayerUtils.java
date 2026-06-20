package io.github.jfglzs.asa.utils;

import fi.dy.masa.malilib.util.InventoryUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static io.github.jfglzs.asa.utils.MCUtils.getPlayer;

public class PlayerUtils {
    public static int getInventoryItemCount(Item item) {
        return getInventory(MCUtils.getPlayer())
                .stream()
                .mapToInt(ItemStack::getCount)
                .sum();
    }

    public static List<ItemStack> getInventory(Player player) {
        Inventory inventory = player.getInventory();
        return IntStream
                .range(0, inventory.getContainerSize())
                .mapToObj(inventory::getItem)
                .toList();
    }

    public static Item getItem(int slotIndex) {
        return getItemStack(slotIndex).getItem();
    }

    public static ItemStack getItemStack(int slotIndex) {
        Player player = getPlayer();
        if (player == null) return ItemStack.EMPTY;
        return player.getInventory().getItem(slotIndex);
    }

    public static List<Integer> getAllBoxIndexes(int maxIndex) {
        List<Integer> shulkerBoxIndexes = new ArrayList<>();
        Player player = getPlayer();
        if (player == null) return shulkerBoxIndexes;
        Inventory inventory = player.getInventory();


        for (int i = 0; i < maxIndex; i++) {
            ItemStack stack = inventory.getItem(i);
            Item item = stack.getItem();

            // 判断是否为潜影盒
            if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock) {
                shulkerBoxIndexes.add(i);
            }
        }

        return shulkerBoxIndexes;
    }

    public static List<Integer> getUnFullBoxIndexes(int indexes) {
        Player player = getPlayer();
        List<Integer> shulkerBoxIndexes = getAllBoxIndexes(indexes);
        List<Integer> UnFullShulkerBoxIndexes = new ArrayList<>();

        for (int i : shulkerBoxIndexes) {
            int isNotAir = 0;
            int loop = 0;
            List<ItemStack> IS = getStoredItems_(player.getInventory().getItem(i));

            for (ItemStack stack : IS) {
                loop++;
                Item item = stack.getItem();
                if (item != Items.AIR) isNotAir++;
                if (isNotAir != 27 && loop == 27 && i > 8) UnFullShulkerBoxIndexes.add(i); //不支持快捷栏直接打开盒子 所以排除快捷栏
            }
        }

        return UnFullShulkerBoxIndexes;
    }

    public static List<Integer> getNotEmptyBoxIndexes(List<Integer> shulkerBoxIndexes) {
        Player player = getPlayer();
        List<Integer> NotEmptyShulkerBoxIndexes = new ArrayList<>();

        for (int i : shulkerBoxIndexes) {
            int emptyOrUnfull = 0;
            int loop = 0;
            List<ItemStack> IS = getStoredItems_(player.getInventory().getItem(i));

            for (ItemStack stack : IS) {
                loop++;
                if (stack.getItem().equals(Items.AIR)) emptyOrUnfull++;
                if (!(emptyOrUnfull == 27) && loop == 27) NotEmptyShulkerBoxIndexes.add(i);
            }
        }

        return NotEmptyShulkerBoxIndexes;
    }

    public static List<Integer> getPureUnFullBoxByItemId(int maxIndex, List<String> whitelist) {
        Player player = getPlayer();
        List<Integer> result = new ArrayList<>();

        for (int slot : getAllBoxIndexes(maxIndex)) {

            ItemStack box = player.getInventory().getItem(slot);
            List<ItemStack> items = getStoredItems_(box);

            String itemId = null;
            boolean valid = true;
            boolean hasItem = false;

            for (ItemStack stack : items) {

                if (stack.isEmpty()) {
                    continue;
                }

                hasItem = true;

                String currentId = MCUtils.getItemID(stack.getItem());

                // 白名单过滤
                if (!whitelist.isEmpty() && !whitelist.contains(currentId)) {
                    valid = false;
                    break;
                }

                if (itemId == null) {
                    itemId = currentId;
                }
                else if (!itemId.equals(currentId)) {
                    valid = false;
                    break;
                }
            }
            // 空盒不要
            if (valid && hasItem && isBoxFull(box)) {
                result.add(slot);
            }
        }

        return result;
    }

    public static List<ItemStack> getStoredItems_(ItemStack box) {
        return InventoryUtils.getStoredItems(box, 27);
    }

    public static boolean isBoxFull(ItemStack box) {
        List<ItemStack> items = getStoredItems_(box);
        return items.stream().filter(stack -> stack.getMaxStackSize() - stack.getCount() != 0).toList().isEmpty();
    }

    public static int checkRemainCount(Item item) {
        int storedCount = getNotEmptyBoxIndexes(getAllBoxIndexes(36)).stream()
                .flatMap(i -> getStoredItems_(getPlayer().getInventory().getItem(i)).stream())
                .filter(j -> j.getItem().equals(item))
                .mapToInt(ItemStack::getCount)
                .sum();

        return storedCount + getInventoryItemCount(item);
    }

    public static ItemStack getPlayerMainHandStack() {
        if (MCUtils.getMinecraftClient().player == null) {
            return ItemStack.EMPTY;
        }
        return MCUtils.getMinecraftClient().player.getMainHandItem();
    }

    public static boolean isSurvivalMode(Player player) {
        return player != null && !player.isCreative() && !player.isSpectator();
    }

    public static boolean isShulkerBox(ItemStack stack) {
        return stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof ShulkerBoxBlock;
    }
}
