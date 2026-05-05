package io.github.jfglzs.asa.utils;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static fi.dy.masa.malilib.util.InventoryUtils.getStoredItems;
import static io.github.jfglzs.asa.utils.MCUtils.getPlayer;

public class PlayerUtils {
    public static int getInventoryItemCount(Item item) {
        PlayerInventory inventory = MCUtils.getPlayer().getInventory();
        int itemCount = IntStream.range(0, inventory.size())
                .mapToObj(inventory::getStack)
                .filter(stack -> stack != null && stack.getItem().equals(item))
                .mapToInt(ItemStack::getCount)
                .sum();
//            if (inventory.getStack(40).getItem().equals(item)) itemCount += inventory.getStack(40).getCount();

        return itemCount;
    }

    public static List<Integer> getAllBoxIndexes(int maxIndex) {
        List<Integer> shulkerBoxIndexes = new ArrayList<>();
        PlayerEntity player = MCUtils.getPlayer();
        if (player == null) return shulkerBoxIndexes;
        PlayerInventory inventory = player.getInventory();


        for (int i = 0; i < maxIndex; i++) {
            ItemStack stack = inventory.getStack(i);
            Item item = stack.getItem();

            // 判断是否为潜影盒
            if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock) {
                shulkerBoxIndexes.add(i);
            }
        }

        return shulkerBoxIndexes;
    }

    public static List<Integer> getUnFullBoxIndexes(List<Integer> boxIndexes) {
        PlayerEntity player = MCUtils.getPlayer();
        List<Integer> shulkerBoxIndexes = boxIndexes;
        List<Integer> emptyList = new ArrayList<>();
        List<Integer> UnFullShulkerBoxIndexes = new ArrayList<>();

        for (int i : shulkerBoxIndexes) {
            int isNotAir = 0;
            int loop = 0;
            int amount = 27;
            DefaultedList<ItemStack> IS = getStoredItems(player.getInventory().getStack(i), amount);

            for (ItemStack stack : IS) {
                loop++;
                if (!stack.getItem().equals(Items.AIR)) isNotAir++;
                if (!(isNotAir == 27) && loop == 27 && i > 8) UnFullShulkerBoxIndexes.add(i); //不支持快捷栏直接打开盒子 所以排除快捷栏
            }
        }

        return UnFullShulkerBoxIndexes;

    }

    public static int getOpenedBoxEmptySlots(int slot) {
        PlayerEntity player = MCUtils.getPlayer();
        int EmptySlots = 0;

        int amount = 27;
//            System.out.println("slot is " + slot);
        if (amount == -1) return -1;

        DefaultedList<ItemStack> IS = getStoredItems(player.getInventory().getStack(slot), amount);

        for (ItemStack stack : IS) {
            if (stack.getItem().equals(Items.AIR)) EmptySlots++;
        }

        return EmptySlots;
    }

    public static List<Integer> getNotEmptyBoxIndexes(List<Integer> shulkerBoxIndexes) {
        PlayerEntity player = MCUtils.getPlayer();
        List<Integer> NotEmptyShulkerBoxIndexes = new ArrayList<>();

        for (int i : shulkerBoxIndexes) {
            int isAir = 0;
            int loop = 0;
            int amount = 27;
            DefaultedList<ItemStack> IS = getStoredItems(player.getInventory().getStack(i), amount);

            for (ItemStack stack : IS) {
                loop++;
                if (stack.getItem().equals(Items.AIR)) isAir++;
                if (!(isAir == 27) && loop == 27) NotEmptyShulkerBoxIndexes.add(i);
            }
        }

        return NotEmptyShulkerBoxIndexes;

    }

    public static Item transfromToItem(String item) {
        //? if > 1.20.1 {
        Identifier identifier = Identifier.ofVanilla(item);
        //?} else {
        /*Identifier identifier = new Identifier("minecraft", item);
        *///?}
        return Registries.ITEM.get(identifier);
    }

    public static boolean isNotAirInMainHand() {
        return MCUtils.getMinecraftClient().player != null && !MCUtils.getMinecraftClient().player.getMainHandStack().getItem().equals(Items.AIR);
    }

    public static int getInventoryEmptySlot() {
        for (int i = 36; i >= 9; i--) {
            if (MCUtils.getPlayer().getInventory().getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public static int checkRemainCount(Item item) {
        int storedCount = getNotEmptyBoxIndexes(getAllBoxIndexes(36)).stream()
                .flatMap(i -> getStoredItems(getPlayer().getInventory().getStack(i), 27).stream())
                .filter(j -> j.getItem().equals(item))
                .mapToInt(ItemStack::getCount)
                .sum();

        return storedCount + getInventoryItemCount(item);
    }

    public static ItemStack getPlayerMainHandStack() {
        if (MCUtils.getMinecraftClient().player == null) {
            return ItemStack.EMPTY;
        }
        return MCUtils.getMinecraftClient().player.getMainHandStack();
    }
}
