package io.github.jfglzs.asa.feature.boxRestock;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.events.OpenScreenEvent;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.Mods;
import io.github.jfglzs.asa.utils.PlayerUtils;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BoxRestockMannager {
    public static BoxRestockContext context = null;
    private static final long AUTO_RESTOCK_TIMEOUT_MS = 1200L;
    private static final long COMPLETION_COOLDOWN_MS = 350L;
    private static long autoRestockTimeoutMs = 0L;
    private static long lastAutoRestockCompletedMs = 0L;
    private static boolean processing = false;

    public static void init() {
        OpenScreenEvent.INSTANCE.register(screen -> {
            if (context == null && screen instanceof ShulkerBoxScreen && Mods.isShulkerBoxLoaded) {
                ThreadUtils.runOnClientEndTick(BoxRestockMannager::process);
            }
        });
    }

    public static boolean canStartAutoRestock(ItemStack stackHand, int threshold) {
        if (!Configs.AUTO_BOX_RESTROKE.getBooleanValue()) return false;
        if (stackHand.isEmpty() || stackHand.getMaxStackSize() == 1) return false;
        if (stackHand.getCount() >= threshold) return false;
        if (isAutoRestockActive()) return false;
        return System.currentTimeMillis() - lastAutoRestockCompletedMs >= COMPLETION_COOLDOWN_MS;
    }

    public static boolean startAutoRestock(ItemStack stackHand, int boxInventorySlot, int threshold) {
        if (!canStartAutoRestock(stackHand, threshold)) return false;
        context = new BoxRestockContext(stackHand.copy(), boxInventorySlot, stackHand.getCount(), threshold);
        processing = false;
        autoRestockTimeoutMs = System.currentTimeMillis() + AUTO_RESTOCK_TIMEOUT_MS;
        return true;
    }

    public static boolean isAutoRestockActive() {
        if (context == null) return false;
        if (System.currentTimeMillis() <= autoRestockTimeoutMs) return true;
        cancelAutoRestock();
        return false;
    }

    public static boolean shouldSuppressShulkerScreen() {
        return isAutoRestockActive();
    }

    public static void cancelAutoRestock() {
        context = null;
        autoRestockTimeoutMs = 0L;
        processing = false;
    }

    private static void completeAutoRestock(LocalPlayer player) {
        lastAutoRestockCompletedMs = System.currentTimeMillis();
        cancelAutoRestock();
        if (player != null) {
            player.closeContainer();
        }
    }

    public static void process() {
        if (context == null || !Configs.AUTO_BOX_RESTROKE.getBooleanValue()) return;

        if (MCUtils.getScreen() instanceof ShulkerBoxScreen boxScreen) {
            var menu = boxScreen.getMenu();
            for (Slot slot : menu.slots) {
                ItemStack slotItem = slot.getItem();
                ItemStack stackHand = context.stackHand;
                if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(slotItem, stackHand) && canMove(slotItem)) {
                    if (slot.index >= 54 && slot.index <= 62) continue;
                    preciselyRestockSelectedHotbarSlot(MCUtils.getMinecraft().player, menu, slot.index);
                    return;
                }
            }
        }
    }

    public static void processOpenContainer() {
        if (!isAutoRestockActive() || !Configs.AUTO_BOX_RESTROKE.getBooleanValue()) return;
        if (processing) return;

        var minecraft = MCUtils.getMinecraft();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.gameMode == null) {
            System.out.println("[ASA BoxRestock] cannot process: missing player or game mode");
            cancelAutoRestock();
            return;
        }

        AbstractContainerMenu menu = player.containerMenu;
        if (menu == null || menu == player.inventoryMenu) {
            System.out.println("[ASA BoxRestock] cannot process: no open container");
            return;
        }

        if (!targetBoxStillMatches(player) || !openedContainerMatchesTargetBox(player, menu)) {
            System.out.println("[ASA BoxRestock] cancel: target shulker slot changed slot=" + context.boxInventorySlot);
            completeAutoRestock(player);
            return;
        }

        processing = true;
        if (hasEnoughInHand(player)) {
            completeAutoRestock(player);
            return;
        }

        int containerSlots = Math.min(27, menu.slots.size());
        System.out.println("[ASA BoxRestock] scanning containerId=" + menu.containerId + " slots=" + menu.slots.size() + " target=" + context.stackHand.getItem());
        for (int slotIndex = 0; slotIndex < containerSlots; slotIndex++) {
            Slot slot = menu.slots.get(slotIndex);
            ItemStack slotItem = slot.getItem();
            ItemStack stackHand = context.stackHand;
            if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(slotItem, stackHand) && canMove(slotItem)) {
                System.out.println("[ASA BoxRestock] restocking selected hotbar from slot=" + slotIndex + " item=" + slotItem.getItem() + " count=" + slotItem.getCount());
                if (!preciselyRestockSelectedHotbarSlot(player, menu, slotIndex)) {
                    completeAutoRestock(player);
                }
                return;
            }
        }
        System.out.println("[ASA BoxRestock] target item not found in opened shulker");
        completeAutoRestock(player);
    }

    private static boolean hasEnoughInHand(LocalPlayer player) {
        if (context == null) return true;
        ItemStack currentHandStack = player.getMainHandItem();
        return fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(currentHandStack, context.stackHand)
                && currentHandStack.getCount() >= context.threshold;
    }

    private static boolean preciselyRestockSelectedHotbarSlot(LocalPlayer player, AbstractContainerMenu menu, int sourceSlotIndex) {
        if (player == null || context == null || MCUtils.getMinecraft().gameMode == null) return false;

        int targetSlotIndex = findSelectedHotbarMenuSlot(player, menu);
        if (sourceSlotIndex < 0 || sourceSlotIndex >= menu.slots.size()) return false;
        if (targetSlotIndex < 0) return false;

        ItemStack sourceStack = menu.slots.get(sourceSlotIndex).getItem();
        ItemStack targetStack = menu.slots.get(targetSlotIndex).getItem();
        if (!fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(sourceStack, context.stackHand)) return false;
        if (!fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(targetStack, context.stackHand)) return false;
        if (targetStack.getCount() >= targetStack.getMaxStackSize()) {
            completeAutoRestock(player);
            return true;
        }

        var gameMode = MCUtils.getMinecraft().gameMode;
        gameMode.handleContainerInput(menu.containerId, sourceSlotIndex, 0, ContainerInput.PICKUP, player);
        gameMode.handleContainerInput(menu.containerId, targetSlotIndex, 0, ContainerInput.PICKUP, player);
        gameMode.handleContainerInput(menu.containerId, sourceSlotIndex, 0, ContainerInput.PICKUP, player);
        completeAutoRestock(player);
        return true;
    }

    private static int findSelectedHotbarMenuSlot(LocalPlayer player, AbstractContainerMenu menu) {
        ItemStack mainHandStack = player.getMainHandItem();
        for (int slotIndex = 54; slotIndex <= 62 && slotIndex < menu.slots.size(); slotIndex++) {
            if (menu.slots.get(slotIndex).getItem() == mainHandStack) {
                return slotIndex;
            }
        }

        for (int slotIndex = 54; slotIndex <= 62 && slotIndex < menu.slots.size(); slotIndex++) {
            ItemStack slotStack = menu.slots.get(slotIndex).getItem();
            if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(slotStack, mainHandStack)
                    && slotStack.getCount() == mainHandStack.getCount()) {
                return slotIndex;
            }
        }
        return -1;
    }

    private static boolean targetBoxStillMatches(LocalPlayer player) {
        if (context == null) return false;
        if (context.boxInventorySlot < 0 || context.boxInventorySlot >= player.getInventory().getContainerSize()) return false;
        ItemStack boxStack = player.getInventory().getItem(context.boxInventorySlot);
        if (!PlayerUtils.isShulkerBox(boxStack)) return false;
        for (ItemStack itemStack : PlayerUtils.getBoxItemStacks(boxStack)) {
            if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(itemStack, context.stackHand)) {
                return true;
            }
        }
        return false;
    }

    private static boolean openedContainerMatchesTargetBox(LocalPlayer player, AbstractContainerMenu menu) {
        if (context == null) return false;
        ItemStack boxStack = player.getInventory().getItem(context.boxInventorySlot);
        var storedStacks = PlayerUtils.getBoxItemStacks(boxStack);
        int slotsToCheck = Math.min(27, Math.min(storedStacks.size(), menu.slots.size()));
        for (int i = 0; i < slotsToCheck; i++) {
            ItemStack stored = storedStacks.get(i);
            ItemStack opened = menu.slots.get(i).getItem();
            if (stored.isEmpty() && opened.isEmpty()) continue;
            if (stored.getCount() != opened.getCount()) return false;
            if (!fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(stored, opened)) return false;
        }
        return true;
    }

    private static boolean canMove(ItemStack stack) {
        Item item = stack.getItem();
        String itemID = MCUtils.getItemID(item);
        if (Configs.ENABLE_AUTO_BOX_RESTROKE_BLACKLIST.getBooleanValue()) {
            return Configs.AUTO_BOX_RESTROKE_BLACKLIST.getStrings().stream().noneMatch(itemID::equals);
        }
        else if (Configs.ENABLE_AUTO_WASTE_CLEAN_WHITELIST.getBooleanValue()) {
            return Configs.AUTO_BOX_RESTROKE_WHITELIST.getStrings().stream().anyMatch(itemID::equals);
        }
        return true;
    }

    public record BoxRestockContext(ItemStack stackHand, int boxInventorySlot, int countBefore, int threshold) {}
}
