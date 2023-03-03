package net.fabricmc.smphack.Hacks.SpeedMine;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.fabricmc.smphack.config.ConfigUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class SpeedMine extends MainGui {

int Nobreakhaste;
String Speedminemode;
boolean Speedmine;

    @Override
    public void toggled()
    {
        assert MinecraftClient.getInstance().player != null;
        assert GeneralConfig.getConfig()!=null;
         Speedminemode= String.valueOf(ConfigUtil.config.SpeedmMineModes);
         Speedmine = GeneralConfig.getConfig().isEnableSpeedmine();
      if (Speedmine) {
          if (Speedminemode.equals("HasteMode")) {
              MinecraftClient.getInstance().player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 100, (Nobreakhaste = GeneralConfig.getConfig().getNobreakHaste()) - 1, true, false, false));
          } else {
              MinecraftClient.getInstance().player.removeStatusEffect(StatusEffects.HASTE);
          }
      }
    }


}
