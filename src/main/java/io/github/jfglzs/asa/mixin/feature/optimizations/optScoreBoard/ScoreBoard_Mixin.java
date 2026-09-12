package io.github.jfglzs.asa.mixin.feature.optimizations.optScoreBoard;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;

@Mixin(Scoreboard.class)
public class ScoreBoard_Mixin {
    @Unique private int asa$tickCount = 0;
    @Unique private Collection<PlayerScoreEntry> asa$cache;

    @WrapMethod(
            method = "listPlayerScores(Lnet/minecraft/world/scores/Objective;)Ljava/util/Collection;"
    )
    public Collection<PlayerScoreEntry> listPlayerScores(Objective objective,
                                                         Operation<Collection<PlayerScoreEntry>> original) {
        if (! Configs.Optimizations.OPT_SCORE_BOARD.getBooleanValue()) return original.call(objective);

        if (asa$tickCount != MCUtils.getTickCount() || asa$cache == null) {
            this.asa$cache = original.call(objective);
            asa$tickCount = MCUtils.getTickCount();
            return asa$cache;
        }
        return asa$cache;
    }
}
