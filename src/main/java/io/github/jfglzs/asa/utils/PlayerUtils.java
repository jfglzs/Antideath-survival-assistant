package io.github.jfglzs.asa.utils;

import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.NonNullList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static fi.dy.masa.malilib.util.InventoryUtils.getStoredItems;
import static io.github.jfglzs.asa.utils.MCUtils.getPlayer;

public class PlayerUtils {
    public static int getInventoryItemCount(Item item) {
        Inventory inventory = MCUtils.getPlayer().getInventory();
        int itemCount = IntStream.range(0, inventory.getContainerSize())
                .mapToObj(inventory::getItem)
                .filter(stack -> stack != null && stack.getItem().equals(item))
                .mapToInt(ItemStack::getCount)
                .sum();
//            if (inventory.getStack(40).getItem().equals(item)) itemCount += inventory.getStack(40).getCount();

        return itemCount;
    }

    public static List<Integer> getAllBoxIndexes(int maxIndex) {
        List<Integer> shulkerBoxIndexes = new ArrayList<>();
        Player player = MCUtils.getPlayer();
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

    public static List<Integer> getUnFullBoxIndexes(List<Integer> boxIndexes) {
        Player player = MCUtils.getPlayer();
        List<Integer> shulkerBoxIndexes = boxIndexes;
        List<Integer> emptyList = new ArrayList<>();
        List<Integer> UnFullShulkerBoxIndexes = new ArrayList<>();

        for (int i : shulkerBoxIndexes) {
            int isNotAir = 0;
            int loop = 0;
            int amount = 27;
            NonNullList<ItemStack> IS = getStoredItems(player.getInventory().getItem(i), amount);

            for (ItemStack stack : IS) {
                loop++;
                if (!stack.getItem().equals(Items.AIR)) isNotAir++;
                if (!(isNotAir == 27) && loop == 27 && i > 8) UnFullShulkerBoxIndexes.add(i); //不支持快捷栏直接打开盒子 所以排除快捷栏
            }
        }

        return UnFullShulkerBoxIndexes;

    }

    public static int getOpenedBoxEmptySlots(int slot) {
        Player player = MCUtils.getPlayer();
        int EmptySlots = 0;

        int amount = 27;
//            System.out.println("slot is " + slot);
        if (amount == -1) return -1;

        NonNullList<ItemStack> IS = getStoredItems(player.getInventory().getItem(slot), amount);

        for (ItemStack stack : IS) {
            if (stack.getItem().equals(Items.AIR)) EmptySlots++;
        }

        return EmptySlots;
    }

    public static List<Integer> getNotEmptyBoxIndexes(List<Integer> shulkerBoxIndexes) {
        Player player = MCUtils.getPlayer();
        List<Integer> NotEmptyShulkerBoxIndexes = new ArrayList<>();

        for (int i : shulkerBoxIndexes) {
            int isAir = 0;
            int loop = 0;
            int amount = 27;
            NonNullList<ItemStack> IS = getStoredItems(player.getInventory().getItem(i), amount);

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
        ResourceLocation identifier = ResourceLocation.withDefaultNamespace(item);
        //?} else {
        /*Identifier identifier = new Identifier("minecraft", item);
         *///?}
        //~ if <=1.21.1 '.getValue(' -> '.get(' {
        return BuiltInRegistries.ITEM.getValue(identifier);
        //~}
    }

    public static boolean isNotAirInMainHand() {
        return MCUtils.getMinecraftClient().player != null && !MCUtils.getMinecraftClient().player.getMainHandItem().getItem().equals(Items.AIR);
    }

    public static int getInventoryEmptySlot() {
        for (int i = 36; i >= 9; i--) {
            if (MCUtils.getPlayer().getInventory().getItem(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public static int checkRemainCount(Item item) {
        int storedCount = getNotEmptyBoxIndexes(getAllBoxIndexes(36)).stream()
                .flatMap(i -> getStoredItems(getPlayer().getInventory().getItem(i), 27).stream())
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
}
