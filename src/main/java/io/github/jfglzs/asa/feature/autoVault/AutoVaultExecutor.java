package io.github.jfglzs.asa.feature.autoVault;

import com.google.common.util.concurrent.RateLimiter;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultState;
import net.minecraft.world.level.block.state.BlockState;

public class AutoVaultExecutor {
    private static final RateLimiter LIMITER = RateLimiter.create(0.6);
    private static final String SPAWN_COMMAND = "player %s spawn at %.2f %.2f %.2f facing %.1f %.1f";
    private static final String KILL_COMMAND = "player %s kill";
    private static final String USE_COMMAND = "player %s use";
    private static ExecutorState executorState = ExecutorState.IDLE;
    private static BlockPos vaultPos;
    private static String prefix;
    private static String name;
    private static int current;
    private static int start;
    private static int end;
    private static float blockX;
    private static float blockY;
    private static float blockZ;
    private static float direction;
    private static float in;
    public static boolean isRunning = false;

    public static boolean setBlockPos(BlockPos pos) {
        ClientLevel level = MCUtils.getLevel();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() == Blocks.VAULT) {
            AutoVaultExecutor.vaultPos = pos;
            return true;
        }
        return false;
    }

    public static void set(String prefix, int start, int end, float blockX, float blockY, float blockZ, float direction, float in) {
        AutoVaultExecutor.direction  = direction;
        AutoVaultExecutor.prefix = prefix;
        AutoVaultExecutor.current = start;
        AutoVaultExecutor.start = start;
        AutoVaultExecutor.blockX = blockX;
        AutoVaultExecutor.blockY = blockY;
        AutoVaultExecutor.blockZ  = blockZ;
        AutoVaultExecutor.end = end;
        AutoVaultExecutor.in = in;
    }

    public static String makeCommand() {
        if (current >= start && current <= end) {
            current++;
        }
        else {
            current = start;
        }

        name = prefix + current;

        return SPAWN_COMMAND.formatted(name, blockX, blockY, blockZ, direction, in);
    }

    public static void reset() {
        AutoVaultExecutor.isRunning = false;
        executorState = ExecutorState.IDLE;
    }

    public static void run() {
        if (!Configs.AUTO_VAULT_COMMAND.getBooleanValue() || !isRunning) return;

        ClientLevel level = MCUtils.getLevel();
        if (level == null) return;
        BlockState state = level.getBlockState(vaultPos);
        if (vaultPos == null || state.getBlock() != Blocks.VAULT) return;
        VaultState vaultState = state.getValue(VaultBlock.STATE);

        if (executorState == ExecutorState.IDLE && canUseVault(vaultState)) {
            if (LIMITER.tryAcquire()) {
                MCUtils.executeCommand(makeCommand());
                executorState = ExecutorState.SPAWNING;
            }
        }
        else if (executorState == ExecutorState.SPAWNING) {
            if (!LIMITER.tryAcquire()) return;
            if (MCUtils.isPlayerOnline(name)) {
                MCUtils.executeCommand(USE_COMMAND.formatted(name));
                executorState = ExecutorState.ON_ACTION;
            }
        }
        else if (canUseVault(vaultState) && executorState == ExecutorState.ON_ACTION) {
            if (LIMITER.tryAcquire()) {
                MCUtils.executeCommand(KILL_COMMAND.formatted(name));
                executorState = ExecutorState.IDLE;
            }
        }
    }

    public static boolean canUseVault(VaultState vaultState) {
        return vaultState == VaultState.INACTIVE || vaultState == VaultState.ACTIVE;
    }

    private enum ExecutorState {
        IDLE(),
        SPAWNING(),
        ON_ACTION(),
    }
}
