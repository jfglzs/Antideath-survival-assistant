package io.github.jfglzs.asa.utils;

import com.mojang.authlib.GameProfile;
import fi.dy.masa.malilib.util.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PlayerUtils {
    public static int getInventoryItemCount(Item item) {
        return getInventory().stream()
                             .filter(stack -> stack.is(item))
                             .mapToInt(ItemStack::getCount)
                             .sum();
    }

    public static List<ItemStack> getInventory() {
        //~ if >= 1.21.5 'items' -> 'getNonEquipmentItems()' {
        return MCUtils.getLocalPlayer()
                      .getInventory()
                      .getNonEquipmentItems();
        //~}
    }

    public static Item getItem(int slotIndex) {
        return getItemStack(slotIndex).getItem();
    }

    public static ItemStack getItemStack(int slotIndex) {
        Player player = MCUtils.getLocalPlayer();
        return player == null ? ItemStack.EMPTY : player.getInventory()
                                                        .getItem(slotIndex);
    }

    public static List<Integer> getAllBoxIndexes(int minIndex, int maxIndex) {
        List<Integer> results = new ArrayList<>();
        Inventory inventory = MCUtils.getLocalPlayer()
                                     .getInventory();

        for (int i = minIndex; i < maxIndex; i++) {
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
            List<ItemStack> boxStacks = getBoxItemStacks(player.getInventory()
                                                               .getItem(i));
            if (! boxStacks.stream()
                           .filter(itemStack -> ! itemStack.isEmpty())
                           .toList()
                           .isEmpty()) {
                results.add(i);
            }
        }

        return results;
    }

    public static List<ItemStack> getBoxItemStacks(ItemStack box) {
        return InventoryUtils.getStoredItems(box, 27);
    }

    public static boolean isBoxFull(ItemStack box) {
        if (! isShulkerBox(box))
            return false;
        // getStoredItems 返回固定 27 槽(含 EMPTY), 需按非空槽判断
        List<ItemStack> items = getBoxItemStacks(box);
        int nonEmpty = 0;
        for (ItemStack stack : items) {
            if (! stack.isEmpty()) {
                nonEmpty++;
            }
        }
        return nonEmpty >= 27;
    }

    public static boolean isBoxEmpty(ItemStack box) {
        if (! isShulkerBox(box))
            return false;
        // getStoredItems 返回固定 27 槽(含 EMPTY), 不能用 List.isEmpty() 判断
        for (ItemStack stack : getBoxItemStacks(box)) {
            if (! stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static int getBoxFreeSlots(ItemStack box) {
        if (! isShulkerBox(box))
            return 0;
        List<ItemStack> items = getBoxItemStacks(box);
        int occupied = 0;
        for (ItemStack stack : items) {
            if (! stack.isEmpty()) {
                occupied++;
            }
        }
        return Math.max(0, 27 - occupied);
    }

    public static int checkRemainCount(Item item) {
        LocalPlayer localPlayer = MCUtils.getLocalPlayer();
        if (localPlayer == null)
            return 0;
        int storedCount = getNotEmptyBoxIndexes(getAllBoxIndexes(0, 36))
                .stream().flatMap(i -> getBoxItemStacks(getInventory().get(i)).stream())
                .filter(j -> j.is(item))
                .mapToInt(ItemStack::getCount)
                .sum();
        return storedCount + getInventoryItemCount(item);
    }

    public static ItemStack getPlayerMainHandStack() {
        return getPlayerHandStack(InteractionHand.MAIN_HAND);
    }

    public static ItemStack getPlayerHandStack(InteractionHand hand) {
        LocalPlayer player = MCUtils.getMinecraft().player;
        return player == null ? ItemStack.EMPTY : player.getItemInHand(hand);
    }

    public static boolean isSurvivalMode() {
        return isSurvivalMode(MCUtils.getLocalPlayer());
    }

    public static boolean isSurvivalMode(Player player) {
        return player != null && ! player.isCreative() && ! player.isSpectator();
    }

    public static boolean isShulkerBox(ItemStack stack) {
        return stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof ShulkerBoxBlock;
    }

    public static void closeContainer() {
        LocalPlayer player = MCUtils.getLocalPlayer();
        if (player != null) {
            player.closeContainer();
        }
    }

    //~ if >= 26.1 'ClickType' -> 'ContainerInput' {
    public static void clickSlot(final int containerId, final int slotNum, final int buttonNum,
                                 final net.minecraft.world.inventory.ContainerInput input, final Player player) {
        //~}
        Minecraft mc = MCUtils.getMinecraft();
        //? if >= 26.1 {
        mc.gameMode.handleContainerInput(containerId, slotNum, buttonNum, input, player);
        //?} else {
        /*mc.gameMode.handleInventoryMouseClick(containerId, slotNum, buttonNum, input, player);
         *///?}
    }

    /**
     * 通过 QUICK_MOVE (shift+点击) 把一个容器槽位的物品快速移动到另一侧
     * 用于潜影盒整理等场景: 目标槽 = 盒子槽, 会把盒子内物品移到背包, 或把背包物品移入盒子
     */
    //~ if >= 26.1 'ClickType' -> 'ContainerInput' {
    public static void quickMove(final int containerId, final int slotNum, final Player player) {
        clickSlot(containerId, slotNum, 0, net.minecraft.world.inventory.ContainerInput.QUICK_MOVE, player);
    }
    //~}

    public static void interactWith(Entity entity, InteractionHand hand) {
        LocalPlayer player = MCUtils.getLocalPlayer();
        Minecraft mc = MCUtils.getMinecraft();
        if (player != null) {
            //? if >= 26.1 {
            mc.gameMode.interact(player, entity, new EntityHitResult(entity), hand);
            //?} else {
            //mc.gameMode.interact(player, entity, hand);
            //?}
        }
    }

    //~ if >=1.21.10 '.getName()' -> '.name()' {
    public static String getName(Player player) {
        return player.getGameProfile()
                     .name();
    }

    public static String getName(GameProfile profile) {
        return profile.name();
    }
    //~}
}
