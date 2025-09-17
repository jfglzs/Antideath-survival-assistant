package io.github.jfglzs.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItem;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

import static fi.dy.masa.malilib.util.InventoryUtils.getStoredItems;
import static io.github.jfglzs.AsaMod.LOGGER;
import static io.github.jfglzs.utils.InventoryUtils.getInventorySlotAmount;
import static io.github.jfglzs.utils.MCUtils.getPlayer;

public class PlayerUtils
{
    public static void test()
    {
//        ItemStack stack = new ItemStack(Items.SHULKER_BOX);
//        CheckAndSend(stack , searchInventory(Items.SHULKER_BOX , 40));
//        System.out.println(getPlayer().getInventory().getEmptySlot());
//        System.out.println(PlayerInventoryUtils.getAllUnFullShulkerBoxIndexes(PlayerInventoryUtils.getAllShulkerBoxIndexes(41)));


    }

    public static class PlayerInventoryUtils
    {
        public static int searchInventory(Item SearchItem , int Index)
        {
            PlayerEntity player = getPlayer();
            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < Index; i++)
            {
                Item item = inventory.getStack(i).getItem();
                ItemStack stack = inventory.getStack(i);
//                System.out.println(item);
                if (item.equals(SearchItem))
                {
//                    int j = stack.getMaxCount();
                    return i;
                }
            }
            return -1;
        }

        public static List<Integer> getAllShulkerBoxIndexes(int maxIndex)
        {
            PlayerEntity player = getPlayer();
            PlayerInventory inventory = player.getInventory();
            List<Integer> shulkerBoxIndexes = new ArrayList<>();

            for (int i = 0; i < maxIndex; i++)
            {
                ItemStack stack = inventory.getStack(i);
                Item item = stack.getItem();

                // 判断是否为潜影盒
                if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock)
                {
                    shulkerBoxIndexes.add(i);
                }
            }

            return shulkerBoxIndexes;
        }

        public static List<Integer> getAllUnFullShulkerBoxIndexes(List<Integer> boxIndexes)
        {
            PlayerEntity player = getPlayer();
            List<Integer> shulkerBoxIndexes = boxIndexes;
            List<Integer> emptyList = new ArrayList<>();
            List<Integer> UnFullShulkerBoxIndexes = new ArrayList<>();

            for(int i : shulkerBoxIndexes)
            {
                int isNotAir = 0;
                int loop = 0;
                int amount = getInventorySlotAmount(player.getInventory().getStack(i));
                if (amount == -1) return emptyList;
                DefaultedList<ItemStack> IS = getStoredItems(player.getInventory().getStack(i), amount);

                for (ItemStack stack : IS)
                {
                    loop++;
                    if (!stack.getItem().equals(Items.AIR)) isNotAir++;
                    if (!(isNotAir == 27) && loop == 27 && i > 8) UnFullShulkerBoxIndexes.add(i); //不支持快捷栏直接打开盒子 所以排除快捷栏
                }
            }

            return UnFullShulkerBoxIndexes;

        }

        public static int getOpenedBoxEmptySlots(int slot)
        {
            PlayerEntity player = getPlayer();
            int EmptySlots = 0;

            int amount = getInventorySlotAmount(player.getInventory().getStack(slot));
//            System.out.println("slot is " + slot);
            if (amount == -1) return -1;

            DefaultedList<ItemStack> IS = getStoredItems(player.getInventory().getStack(slot), amount);

            for (ItemStack stack : IS)
            {
                if (stack.getItem().equals(Items.AIR)) EmptySlots++;
            }

            return EmptySlots;
        }

    }
}
