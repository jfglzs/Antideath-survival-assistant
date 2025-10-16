package io.github.jfglzs.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItem;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static fi.dy.masa.malilib.util.InventoryUtils.getStoredItems;
import static io.github.jfglzs.utils.ContainerUtils.getInventorySlotAmount;
import static io.github.jfglzs.utils.MCUtils.*;
import static io.github.jfglzs.utils.ScreenUtils.openAndGetHandle;
import static io.github.jfglzs.utils.ScreenUtils.refreshScreen;

public class PlayerUtils
{
    public static class PlayerInventoryUtils
    {

        public static boolean clickSlot(MinecraftClient client, int itemIndex, int button , SlotActionType type)
        {
            ScreenHandler screenHandler = openAndGetHandle(new InventoryScreen(getPlayer()));
            if (!openAndCheckScreen()) return false;
            int id =  screenHandler.slots.get(itemIndex).id;
            client.interactionManager.clickSlot(screenHandler.syncId, id , button, type, client.player);
            refreshScreen();
            return true;
        }

        public static int searchInventory(Item searchItem)
        {
            PlayerEntity player = getPlayer();
            PlayerInventory inventory = player.getInventory();

            int index = IntStream.range(0, 36)
                    .filter(i -> {
                        ItemStack stack = inventory.getStack(i);
                        return stack.getItem().equals(searchItem);
                    })
                    .findFirst()
                    .orElse(-1);


//            for (int i = 0; i < 36; i++)
//            {
//                Item item = inventory.getStack(i).getItem();
//                if (item.equals(searchItem))
//                {
//                    return i; //返回物品的索引
//                }
//            }
                return index;
        }

        public static int getInventoryItemCount(Item item)
        {
            PlayerInventory inventory = getPlayer().getInventory();
            int itemCount = IntStream.range(0, inventory.size())
                    .mapToObj(inventory::getStack)
                    .filter(stack -> stack != null && stack.getItem().equals(item))
                    .mapToInt(ItemStack::getCount)
                    .sum();
//            if (inventory.getStack(40).getItem().equals(item)) itemCount += inventory.getStack(40).getCount();

            return itemCount;
        }

        public static List<Integer> getAllBoxIndexes(int maxIndex)
        {
            List<Integer> shulkerBoxIndexes = new ArrayList<>();
            PlayerEntity player = getPlayer();
            if (player == null) return shulkerBoxIndexes;
            PlayerInventory inventory = player.getInventory();


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

        public static List<Integer> getUnFullBoxIndexes(List<Integer> boxIndexes)
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

        public static List<Integer> getNotEmptyBoxIndexes(List<Integer> shulkerBoxIndexes)
        {
            PlayerEntity player = getPlayer();
            List<Integer> NotEmptyShulkerBoxIndexes = new ArrayList<>();

            for(int i : shulkerBoxIndexes)
            {
                int isAir = 0;
                int loop = 0;
                int amount = getInventorySlotAmount(player.getInventory().getStack(i));
                if (amount == -1) return shulkerBoxIndexes;
                DefaultedList<ItemStack> IS = getStoredItems(player.getInventory().getStack(i), amount);

                for (ItemStack stack : IS)
                {
                    loop++;
                    if (stack.getItem().equals(Items.AIR)) isAir++;
                    if (!(isAir == 27) && loop == 27) NotEmptyShulkerBoxIndexes.add(i);
                }
            }

            return NotEmptyShulkerBoxIndexes;

        }

        public static Item transfromToItem(String item)
        {
            //#if MC > 12001
            Identifier identifier = Identifier.ofVanilla(item);
            //#else
            //$$ Identifier identifier = new Identifier("minecraft", item);
            //#endif
            return Registries.ITEM.get(identifier);
        }

        public static boolean isNotAirInMainHand()
        {
            return getMinecraftClient().player != null && !getMinecraftClient().player.getMainHandStack().getItem().equals(Items.AIR);
        }

        public static int getInventoryEmptySlot()
        {
            for (int i = 36; i >= 9 ; i--) // 排除快捷栏 副手 装备栏
            {
                if (getPlayer().getInventory().getStack(i).isEmpty())
                {
                    return i;
                }
            }

            return -1;
        }


    }
}
