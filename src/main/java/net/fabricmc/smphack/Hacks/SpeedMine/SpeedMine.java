package net.fabricmc.smphack.Hacks.SpeedMine;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.Objects;

public class SpeedMine extends MainGui {

int Nobreakhaste;
String Speedmode;
boolean Speedmine;


    @Override
    public void toggled()
    {
        assert MinecraftClient.getInstance().player != null;
        assert GeneralConfig.getConfig()!=null;
         Speedmode= GeneralConfig.getConfig().getSpeedMineMode();
         Speedmine = GeneralConfig.getConfig().isEnableSpeedmine();
      if (Speedmine) {
          if (Objects.equals(Speedmode, "Hastemode"))
          {
              MinecraftClient.getInstance().player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 5, (Nobreakhaste = GeneralConfig.getConfig().getNobreakHaste()) - 1));
          }
          else
          {
              MinecraftClient.getInstance().player.removeStatusEffect(StatusEffects.HASTE);
          }
      }
      else
      {
          MinecraftClient.getInstance().player.removeStatusEffect(StatusEffects.HASTE);
      }
    }


}
