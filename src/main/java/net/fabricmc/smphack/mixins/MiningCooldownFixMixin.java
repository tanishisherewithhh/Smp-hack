package net.fabricmc.smphack.mixins;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.config.ConfigUtil;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.Objects;

//SpeedMine Mixin
@Mixin(ClientPlayerInteractionManager.class)
public abstract class MiningCooldownFixMixin {
    String SpeedMode;
    boolean Speedmine;

    @ModifyConstant(method = "updateBlockBreakingProgress", constant = @Constant(intValue = 5))

    private int MiningCooldownFix(int value) {
        assert GeneralConfig.getConfig() != null;
        SpeedMode = String.valueOf(ConfigUtil.config.SpeedmMineModes);
        Speedmine = GeneralConfig.getConfig().isEnableSpeedmine();
        if (Speedmine) {
            if (Objects.equals(SpeedMode, "NoBreakDelay")) {
                return 0;
            } else {
                return value;
            }
        }
        else {
            return value;
        }
    }
}