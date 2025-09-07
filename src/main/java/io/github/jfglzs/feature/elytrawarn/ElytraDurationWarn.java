package io.github.jfglzs.feature.elytrawarn;

import net.minecraft.sound.SoundEvents;

import static io.github.jfglzs.config.Configs.ELYTRA_WARN_DUR;
import static io.github.jfglzs.utils.ChatUtils.sendMessWithSound;
import static io.github.jfglzs.utils.PlayerUtils.getElytraDuration;

public class ElytraDurationWarn
{
    public static void warnPlayer()
    {
        if (getElytraDuration() == -1) return;

        if (getElytraDuration() <= ELYTRA_WARN_DUR.getIntegerValue())
        {
            sendMessWithSound("§c鞘翅耐久警告！", SoundEvents.ENTITY_VILLAGER_DEATH ,1 ,1);
        }
    }
}
