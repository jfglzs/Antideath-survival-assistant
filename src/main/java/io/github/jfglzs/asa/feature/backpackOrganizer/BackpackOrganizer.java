package io.github.jfglzs.asa.feature.backpackOrganizer;

import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.boxRestock.BoxRestockMannager;
import io.github.jfglzs.asa.feature.boxSplitter.BoxSplitter;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.Mods;
import io.github.jfglzs.asa.utils.PlayerUtils;
import io.github.jfglzs.asa.utils.ShulkerUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * 整理背包杂物 (依赖快捷潜影盒 quickshulker)
 *
 * 目标: 一键整理 —— 把散落在背包/快捷栏的杂物收进排序靠前的潜影盒, 并把后排潜影盒里的
 *       物品向前合并(以背包空位作中转), 尽量腾出后排的空潜影盒, 类似"一件整理"。
 *
 * 机制限制: 游戏同一时间只能打开一个潜影盒容器, 跨盒搬运只能:
 *   开"源盒"把内容 shift 到玩家背包(中转) -> 关盒 -> 开"目标盒"把中转物品收进去。
 *
 * 状态机:
 *   openedSlot      当前等待/正在处理的盒槽(-1 = 空闲)
 *   drainMode       true  = 把 openedSlot 这个盒的内容全部 shift 到背包(腾空源盒)
 *                   false = 把背包/快捷栏里的可收纳散物 shift 进 openedSlot(装填前盒)
 * 盒子界面由 quickshulker 打开; 内容同步后 handleContainerContent 触发 onContainerContent()
 * 执行一步并关盒; 未在超时内打开则看门狗中止; 连续无进展自动停止, 避免死循环。
 */
public class BackpackOrganizer {
    /** 参与整理的背包槽范围(0..35: 快捷栏+主物品栏) */
    private static final int SLOT_MIN = 0;
    private static final int SLOT_MAX = 36;
    /** 潜影盒只从主物品栏(9..35)里找, 不动快捷栏里的盒子 */
    private static final int BOX_MIN = 9;
    private static final int BOX_MAX = 36;
    /** 打开盒子后等待界面/内容同步的最大 tick 数(约3秒), 超过则中止 */
    private static final int OPEN_TIMEOUT = 60;
    /** 连续无进展达到该次数则中止, 防止不可完成时死循环 */
    private static final int MAX_NO_PROGRESS = 16;
    /** 关闭盒子后等待服务器回写背包/盒数据的 tick 数 */
    private static final int SYNC_COOLDOWN = 5;

    private static boolean running = false;
    /** 当前正等待打开/正在处理的盒槽; -1 = 空闲 */
    private static int openedSlot = -1;
    /** 当前打开盒的处理模式: true=腾空源盒, false=装填(收散物) */
    private static boolean drainMode = false;
    /** 打开盒后经过的 tick 数 */
    private static int openWaitTicks = 0;
    /** 关闭盒子后的同步等待 tick 数 */
    private static int syncCooldown = 0;
    /** 连续无实际移动的轮次 */
    private static int noProgressRounds = 0;
    /** 本轮中已判定不可用/已处理完的盒槽(避免反复尝试同一盒) */
    private static final Set<Integer> SKIPPED_SLOTS = new HashSet<>();

    private static void log(String msg) {
        AsaMod.LOGGER.info("[BackpackOrganizer] {}", msg);
    }

    /* ---------------- 对外入口 ---------------- */

    /** 快捷键入口: 未运行则开始, 运行中则停止 */
    public static void toggle() {
        if (running) {
            stop("已停止整理");
        }
        else {
            start();
        }
    }

    public static void start() {
        if (! Mods.quickshulker) {
            ChatUtils.actionBar(ChatUtils.c("整理背包需要安装快捷潜影盒"));
            return;
        }
        LocalPlayer player = MCUtils.getLocalPlayer();
        if (player == null || ! PlayerUtils.isSurvivalMode(player)) {
            ChatUtils.actionBar(ChatUtils.c("仅生存模式可用"));
            return;
        }
        if (BoxSplitter.isRunning()) {
            ChatUtils.actionBar(ChatUtils.c("请先停止盒物品分离器"));
            return;
        }
        if (BoxRestockMannager.context != null) {
            ChatUtils.actionBar(ChatUtils.c("请先停止自动盒子补货"));
            return;
        }
        if (Configs.Functions.ENABLE_AUTO_WASTE_CLEAN.getBooleanValue()) {
            ChatUtils.actionBar(ChatUtils.c("请先关闭自动垃圾清理, 避免冲突"));
            return;
        }
        Screen screen = MCUtils.getScreen();
        if (screen != null && ! (screen instanceof ShulkerBoxScreen)) {
            ChatUtils.actionBar(ChatUtils.c("请先关闭当前界面"));
            return;
        }

        running = true;
        openedSlot = -1;
        drainMode = false;
        openWaitTicks = 0;
        syncCooldown = 0;
        noProgressRounds = 0;
        SKIPPED_SLOTS.clear();
        log("start: survival=" + PlayerUtils.isSurvivalMode(player) + " quickshulker=" + Mods.quickshulker);
        ChatUtils.actionBar(ChatUtils.c("开始整理背包 (再次按下停止)"));
    }

    public static void stop(String message) {
        running = false;
        openedSlot = -1;
        drainMode = false;
        openWaitTicks = 0;
        syncCooldown = 0;
        noProgressRounds = 0;
        SKIPPED_SLOTS.clear();
        if (message != null) {
            ChatUtils.actionBar(ChatUtils.c(message));
        }
    }

    /* ---------------- 事件回调 ---------------- */

    /** 每 tick 驱动状态机 */
    public static void tick() {
        if (! running)
            return;

        if (openedSlot == -1) {
            // 空闲
            if (MCUtils.getScreen() != null)
                return;
            if (syncCooldown > 0) {
                syncCooldown--;
                return;
            }
            if (noProgressRounds >= MAX_NO_PROGRESS) {
                stop("长时间没有进展, 已停止");
                return;
            }
            if (! tryAdvance()) {
                finish();
            }
            return;
        }

        // 等待盒子界面/内容同步
        openWaitTicks++;
        if (MCUtils.getScreen() instanceof ShulkerBoxScreen) {
            if (openWaitTicks > OPEN_TIMEOUT) {
                log("tick: content sync timeout for slot " + openedSlot + ", skip");
                SKIPPED_SLOTS.add(openedSlot);
                PlayerUtils.closeContainer();
                openedSlot = -1;
                openWaitTicks = 0;
            }
            return;
        }
        if (openWaitTicks > OPEN_TIMEOUT) {
            log("tick: open timeout for slot " + openedSlot + ", skip");
            SKIPPED_SLOTS.add(openedSlot);
            openedSlot = -1;
            openWaitTicks = 0;
        }
    }

    /** 盒子内容同步后由事件钩子调用(与 BoxSplitter/BoxRestock 相同) */
    public static void onContainerContent() {
        if (! running || openedSlot == -1) {
            log("onContainerContent ignored: running=" + running + " openedSlot=" + openedSlot);
            return;
        }
        if (! (MCUtils.getScreen() instanceof ShulkerBoxScreen boxScreen)) {
            log("onContainerContent: no ShulkerBoxScreen");
            return;
        }
        if (BoxSplitter.isRunning() || BoxRestockMannager.context != null) {
            log("onContainerContent: conflict, stop");
            stop("检测到其它盒子操作, 整理已停止");
            return;
        }

        log("onContainerContent: openedSlot=" + openedSlot + " drainMode=" + drainMode);
        actOnOpenBox(boxScreen);
    }

    /* ---------------- 核心逻辑 ---------------- */

    /** 空闲时规划下一步并打开盒子; 返回 false = 无可做 */
    private static boolean tryAdvance() {
        LocalPlayer player = MCUtils.getLocalPlayer();
        if (player == null)
            return false;
        dumpState();

        // 阶段1: 有散物可收 且 存在有空间的前排盒 -> 装填最靠前的盒
        int loose = countLooseItems();
        if (loose > 0) {
            int target = findFrontBoxWithSpace();
            log("phase1: loose=" + loose + " targetBox=" + target);
            if (target != -1) {
                openBox(target, false);
                return true;
            }
            // 没有盒能装(所有盒满/被跳过) -> 散物保留, 结束
            ChatUtils.actionBar(ChatUtils.c("没有可收纳的潜影盒空位, 散物保留"));
            return false;
        }

        // 阶段2: 无散物 -> 尝试腾空一个后排盒: 把它的内容搬进前面的盒
        int source = findRearBoxToDrain();
        log("phase2: loose=" + loose + " sourceBox=" + source);
        if (source != -1) {
            openBox(source, true); // 腾空模式: 把源盒内容 shift 到背包
            return true;
        }

        return false;
    }

    private static void dumpState() {
        int loose = countLooseItems();
        int boxes = 0, nonEmpty = 0, freeInBoxes = 0;
        for (int i = BOX_MIN; i < BOX_MAX; i++) {
            ItemStack s = PlayerUtils.getInventory().get(i);
            if (! s.isEmpty() && PlayerUtils.isShulkerBox(s)) {
                boxes++;
                if (! PlayerUtils.isBoxEmpty(s)) nonEmpty++;
                freeInBoxes += PlayerUtils.getBoxFreeSlots(s);
            }
        }
        log(String.format("state: loose=%d boxes=%d nonEmpty=%d boxFree=%d invFree=%d",
                          loose, boxes, nonEmpty, freeInBoxes, countPlayerFreeSlots()));
    }

    /** 对当前打开的盒执行一步(腾空或装填), 然后关盒 */
    private static void actOnOpenBox(ShulkerBoxScreen boxScreen) {
        LocalPlayer player = MCUtils.getLocalPlayer();
        if (player == null)
            return;
        var menu = boxScreen.getMenu();
        log("actOnOpenBox: mode=" + (drainMode ? "drain" : "fill")
            + " slots=" + menu.slots.size() + " containerId=" + menu.containerId);

        int moved = 0;
        if (drainMode) {
            // 腾空源盒: 把盒内物品 shift 到玩家背包(中转)。
            // 受背包空位限制, 一次最多搬 freeSlots 组; 剩余的下一轮再搬(渐进腾空)。
            int freeInv = countPlayerFreeSlots();
            for (Slot slot : menu.slots) {
                if (moved >= freeInv)
                    break;
                if (slot.container instanceof Inventory)
                    continue;
                if (! slot.getItem().isEmpty()) {
                    PlayerUtils.quickMove(menu.containerId, slot.index, player);
                    moved++;
                }
            }
        }
        else {
            // 装填: 把玩家 0..35 里的可收纳散物 shift 进盒
            for (int slotIndex = SLOT_MIN; slotIndex < SLOT_MAX; slotIndex++) {
                ItemStack stack = player.getInventory().getItem(slotIndex);
                if (! isOrganizable(stack))
                    continue;
                for (Slot slot : menu.slots) {
                    if (slot.container instanceof Inventory && slot.getContainerSlot() == slotIndex) {
                        PlayerUtils.quickMove(menu.containerId, slot.index, player);
                        moved++;
                        break;
                    }
                }
            }
        }

        log("actOnOpenBox: moved=" + moved + ", closing");
        PlayerUtils.closeContainer();
        openedSlot = -1;
        openWaitTicks = 0;
        syncCooldown = SYNC_COOLDOWN;

        if (moved == 0) {
            noProgressRounds++;
        }
        else {
            noProgressRounds = 0;
        }
    }

    private static void openBox(int boxSlot, boolean drain) {
        if (boxSlot < BOX_MIN || boxSlot >= BOX_MAX)
            return;
        log("openBox: slot=" + boxSlot + " drain=" + drain);
        openedSlot = boxSlot;
        drainMode = drain;
        openWaitTicks = 0;
        ShulkerUtils.open(boxSlot);
    }

    /* ---------------- 阶段1: 找可装填的前排盒 ---------------- */

    /** 最靠前、有空位、且未跳过的盒槽; 没有返回 -1 */
    private static int findFrontBoxWithSpace() {
        for (int i = BOX_MIN; i < BOX_MAX; i++) {
            if (SKIPPED_SLOTS.contains(i))
                continue;
            ItemStack stack = PlayerUtils.getInventory().get(i);
            if (! stack.isEmpty() && PlayerUtils.isShulkerBox(stack) && PlayerUtils.getBoxFreeSlots(stack) > 0) {
                return i;
            }
        }
        return -1;
    }

    /* ---------------- 阶段2: 找可腾空的后排盒 ---------------- */

    /** 从后往前找一个"可整盒腾空"的后排非空盒 */
    private static int findRearBoxToDrain() {
        for (int i = BOX_MAX - 1; i >= BOX_MIN; i--) {
            if (SKIPPED_SLOTS.contains(i))
                continue;
            ItemStack stack = PlayerUtils.getInventory().get(i);
            if (stack.isEmpty() || ! PlayerUtils.isShulkerBox(stack))
                continue;
            if (PlayerUtils.isBoxEmpty(stack))
                continue;
            if (canDrainBox(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 判断某盒是否值得作为"源盒"被往前搬运:
     *  - 它前方(BOX_MIN..boxSlot)存在"还有空位的前排盒"(搬去的地方);
     *  - 玩家背包(0..35)至少有 1 个空位(逐组中转, 1 个空位即可周转)。
     * 不再要求整盒一次腾空: 每次开盒只搬 背包空位数 组, 多轮渐进直至该盒腾空。
     */
    private static boolean canDrainBox(int boxSlot) {
        ItemStack box = PlayerUtils.getInventory().get(boxSlot);
        if (box.isEmpty() || PlayerUtils.isBoxEmpty(box))
            return false;

        if (countMovableItems(box) == 0)
            return false;

        // 前方盒是否有空位
        boolean hasFrontSpace = false;
        for (int i = BOX_MIN; i < boxSlot; i++) {
            if (SKIPPED_SLOTS.contains(i))
                continue;
            ItemStack s = PlayerUtils.getInventory().get(i);
            if (! s.isEmpty() && PlayerUtils.isShulkerBox(s) && PlayerUtils.getBoxFreeSlots(s) > 0) {
                hasFrontSpace = true;
                break;
            }
        }
        if (! hasFrontSpace)
            return false;

        return countPlayerFreeSlots() >= 1;
    }

    /** 盒内非空槽数(需要搬动的组数) */
    private static int countMovableItems(ItemStack box) {
        int n = 0;
        for (ItemStack s : PlayerUtils.getBoxItemStacks(box)) {
            if (! s.isEmpty()) {
                n++;
            }
        }
        return n;
    }

    /* ---------------- 判定辅助 ---------------- */

    /** 0..35 内"可收纳散物"数量 */
    private static int countLooseItems() {
        LocalPlayer player = MCUtils.getLocalPlayer();
        if (player == null)
            return 0;
        int n = 0;
        for (int i = SLOT_MIN; i < SLOT_MAX; i++) {
            if (isOrganizable(player.getInventory().getItem(i))) {
                n++;
            }
        }
        return n;
    }

    private static boolean hasLooseItems() {
        return countLooseItems() > 0;
    }

    /** 玩家 0..35 空位数(中转空间) */
    private static int countPlayerFreeSlots() {
        LocalPlayer player = MCUtils.getLocalPlayer();
        if (player == null)
            return 0;
        int free = 0;
        for (int i = SLOT_MIN; i < SLOT_MAX; i++) {
            if (player.getInventory().getItem(i).isEmpty()) {
                free++;
            }
        }
        return free;
    }

    /** 可收纳 = 非空 + 非潜影盒 + 未被忽略 */
    private static boolean isOrganizable(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        if (PlayerUtils.isShulkerBox(stack))
            return false;
        return ! isIgnored(stack);
    }

    /** 忽略判定: (可选)忽略工具与盔甲 + 黑名单优先 + 白名单 */
    private static boolean isIgnored(ItemStack stack) {
        if (stack.isEmpty())
            return true;
        if (Configs.Organize.IGNORE_TOOLS_ARMOR.getBooleanValue() && stack.isDamageableItem()) {
            return true;
        }
        String id = MCUtils.getItemID(stack.getItem());
        if (Configs.Organize.ENABLE_ORGANIZE_BLACKLIST.getBooleanValue()) {
            return Configs.isInList(id, Configs.Organize.ORGANIZE_BLACKLIST);
        }
        if (Configs.Organize.ENABLE_ORGANIZE_WHITELIST.getBooleanValue()) {
            return ! Configs.isInList(id, Configs.Organize.ORGANIZE_WHITELIST);
        }
        return false;
    }

    private static void finish() {
        int boxCount = 0;
        int nonEmpty = 0;
        for (int i = BOX_MIN; i < BOX_MAX; i++) {
            ItemStack stack = PlayerUtils.getInventory().get(i);
            if (! stack.isEmpty() && PlayerUtils.isShulkerBox(stack)) {
                boxCount++;
                if (! PlayerUtils.isBoxEmpty(stack)) {
                    nonEmpty++;
                }
            }
        }
        int loose = countLooseItems();
        log("finish: boxCount=" + boxCount + " nonEmpty=" + nonEmpty + " loose=" + loose);
        stop("整理完成: 盒 " + boxCount + ", 非空 " + nonEmpty + ", 散物 " + loose);
    }
}
