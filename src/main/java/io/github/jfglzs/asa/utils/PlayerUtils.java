package io.github.jfglzs.asa.utils;

import fi.dy.masa.malilib.util.InventoryUtils;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PlayerUtils {
    public static int getInventoryItemCount(Item item) {
        return getInventory()
                .stream()
                .filter(stack -> stack.getItem() == item)
                .mapToInt(ItemStack::getCount)
                .sum();
    }

    public static List<ItemStack> getInventory() {
        Inventory inventory = MCUtils.getLocalPlayer().getInventory();
        return IntStream
                .range(0, inventory.getContainerSize())
                .mapToObj(inventory::getItem)
                .toList();
    }

    public static Item getItem(int slotIndex) {
        return getItemStack(slotIndex).getItem();
    }

    public static ItemStack getItemStack(int slotIndex) {
        Player player = MCUtils.getLocalPlayer();
        return player == null ? ItemStack.EMPTY : player.getInventory().getItem(slotIndex);
    }

    public static List<Integer> getAllBoxIndexes(int maxIndex) {
        List<Integer> results = new ArrayList<>();
        Inventory inventory = MCUtils.getLocalPlayer().getInventory();


        for (int i = 0; i < maxIndex; i++) {
            ItemStack stack = inventory.getItem(i);
            if (isShulkerBox(stack)) {
                results.add(i);
            }
        }

        return results;
    }

    public static List<Integer> getNotEmptyBoxIndexes(List<Integer> shulkerBoxIndexes) {
        Player player = MCUtils.getLocalPlayer();
        List<Integer> results = new ArrayList<>();

        for (int i : shulkerBoxIndexes) {
            List<ItemStack> boxStacks = getBoxItemStacks(player.getInventory().getItem(i));
            if (!boxStacks.stream().filter(itemStack -> !itemStack.isEmpty()).toList().isEmpty()) {
                results.add(i);
            }
        }

        return results;
    }

    public static List<ItemStack> getBoxItemStacks(ItemStack box) {
        return InventoryUtils.getStoredItems(box, 27);
    }

    public static boolean isBoxFull(ItemStack box) {
        List<ItemStack> items = getBoxItemStacks(box);
        return items.stream().filter(stack -> stack.getMaxStackSize() - stack.getCount() != 0).toList().isEmpty();
    }

    public static int checkRemainCount(Item item) {
        int storedCount = getNotEmptyBoxIndexes(getAllBoxIndexes(36))
                .stream()
                .flatMap(i -> getBoxItemStacks(MCUtils.getLocalPlayer()
                        .getInventory()
                        .getItem(i))
                        .stream()
                )
                .filter(j -> j.getItem().equals(item))
                .mapToInt(ItemStack::getCount)
                .sum();
        return storedCount + getInventoryItemCount(item);
    }

    public static ItemStack getPlayerMainHandStack() {
        Player player = MCUtils.getMinecraft().player;
        return player == null ? ItemStack.EMPTY : player.getMainHandItem();
    }

    public static boolean isSurvivalMode(Player player) {
        return player != null && !player.isCreative() && !player.isSpectator();
    }

    public static boolean isShulkerBox(ItemStack stack) {
        return stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof ShulkerBoxBlock;
    }
}
